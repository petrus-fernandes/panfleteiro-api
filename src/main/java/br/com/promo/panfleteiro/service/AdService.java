package br.com.promo.panfleteiro.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.promo.panfleteiro.entity.Market;
import br.com.promo.panfleteiro.entity.FlyerSection;
import br.com.promo.panfleteiro.entity.Flyer;
import br.com.promo.panfleteiro.exception.ResourceNotFoundException;
import br.com.promo.panfleteiro.helper.AdFlyerSectionHelper;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.promo.panfleteiro.entity.Ad;
import br.com.promo.panfleteiro.repository.AdRepository;
import br.com.promo.panfleteiro.request.AdRequest;
import br.com.promo.panfleteiro.response.AdResponse;

@Service
public class AdService {

    private AdRepository adRepository;

    private AdFlyerSectionHelper adFlyerSectionHelper;

    private ProductService productService;

    public AdService(AdRepository adRepository, AdFlyerSectionHelper adFlyerSectionHelper, ProductService productService) {
        this.adRepository = adRepository;
        this.adFlyerSectionHelper = adFlyerSectionHelper;
        this.productService = productService;
    }

    public Ad create(AdRequest adRequest) {
        Ad ad = new Ad();
        ad.setPrice(adRequest.getPrice());
        ad.setActive(adRequest.getActive());
        ad.setUrl(adRequest.getUrl());
        return adRepository.save(ad);
    }

    public List<AdResponse> list() {
        return adRepository.findAll().stream().map(this::convertToAdResponse).collect(Collectors.toList());
    }
    
    public void delete(Ad ad) {
        adRepository.delete(ad);
    }

    public AdResponse convertToAdResponse(Ad ad) {
        AdResponse adResponse = createSimpleAdResponse(ad);
        if (ad.getFlyerSection() != null) {
            adResponse.setFlyerSectionId(ad.getFlyerSection().getId());
            adResponse.setMarketsId(ad.getFlyerSection().getMarkets().stream().map(Market::getId).collect(Collectors.toList()));
            adResponse.setInitialDate(getAdInitialDate(ad));
            adResponse.setExpirationDate(getAdExpirationDate(ad));
        }
        return adResponse;
    }

    @NotNull
    private static AdResponse createSimpleAdResponse(Ad ad) {
        AdResponse adResponse = new AdResponse();
        adResponse.setId(ad.getId());
        adResponse.setUrl(ad.getUrl());
        adResponse.setPrice(ad.getPrice());
        adResponse.setActive(ad.getActive());
        adResponse.setProductId(ad.getProduct().getId());
        adResponse.setProductName(ad.getProduct().getName());
        return adResponse;
    }

    public Ad findById(Long id) {
        return adRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ad not found with ID: " + id));
    }

    public AdResponse getAdResponseById(Long id) {
        return convertToAdResponse(findById(id));
    }

    public Page<Ad> findAdsByProductName(String productName, Pageable pageable) {
        return adRepository.findAdsByProductName(productName, pageable);
    }

    public Ad saveAd(Ad ad) {
        return adRepository.save(ad);
    }

    public Page<Ad> findAdsByDistance(Double latitude, Double longitude, Long rangeInKm, Pageable pageable) {
        Map<String, Double> boundingBox = BoundingBoxCalculator.calculateBoundingBox(latitude, longitude, rangeInKm);
        return adRepository.findAdsByDistanceWithBoundingBox(boundingBox.get("minLat"), boundingBox.get("maxLat"), boundingBox.get("minLon"),
                boundingBox.get("maxLon"), latitude, longitude, rangeInKm, pageable);
    }
    public AdResponse convertToAdResponseWithUniqueMarketAndDistance(Ad ad, Long marketId, Double distance) {
        AdResponse adResponse = createSimpleAdResponse(ad);
        adResponse.setDistance(distance);
        if (ad.getFlyerSection() != null) {
            adResponse.setFlyerSectionId(ad.getFlyerSection().getId());
            adResponse.setMarketsId(ad.getFlyerSection().getMarkets().stream().map(Market::getId).filter(id -> id.equals(marketId)).toList());
            adResponse.setInitialDate(getAdInitialDate(ad));
            adResponse.setExpirationDate(getAdExpirationDate(ad));
        }
        return adResponse;
    }

    private Date getAdExpirationDate(Ad ad) {
        return Optional.ofNullable(ad.getFlyerSection())
                .map(FlyerSection::getExpirationDate)
                .orElseGet(() -> Optional.ofNullable(ad.getFlyerSection())
                        .map(FlyerSection::getFlyer)
                        .map(Flyer::getExpirationDate)
                        .orElse(null));
    }

    private Date getAdInitialDate(Ad ad) {
        return Optional.ofNullable(ad.getFlyerSection())
                .map(FlyerSection::getInitialDate)
                .orElseGet(() -> Optional.ofNullable(ad.getFlyerSection())
                        .map(FlyerSection::getFlyer)
                        .map(Flyer::getInitialDate)
                        .orElse(null));
    }

    public Page<AdResponse> findAdsByProductNameAndDistance(Double latitude, Double longitude, Long rangeInKm, Pageable pageable, String productName) {
        Map<String, Double> boundingBox = BoundingBoxCalculator.calculateBoundingBox(latitude, longitude, rangeInKm);

        // Busca os anúncios com distâncias
        Page<Object[]> adsWithDistance = adRepository.findAdsByProductNameAndDistanceWithBoundingBox(
                boundingBox.get("minLat"), boundingBox.get("maxLat"),
                boundingBox.get("minLon"), boundingBox.get("maxLon"),
                latitude, longitude, rangeInKm, productName, pageable);

        // Converte os resultados para AdResponse
        List<AdResponse> adResponses = adsWithDistance.getContent().stream()
                .map(result -> {
                    Ad ad = (Ad) result[0];
                    Double distance = (Double) result[1];
                    AdResponse response = convertToAdResponse(ad);
                    response.setDistance(distance); // Adiciona a distância ao AdResponse
                    return response;
                })
                .collect(Collectors.toList());

        // Retorna uma página de AdResponse
        return new PageImpl<>(adResponses, pageable, adsWithDistance.getTotalElements());
    }

    public List<Ad> getActiveAds() {
        return adRepository.findByActive(true);
    }
}
