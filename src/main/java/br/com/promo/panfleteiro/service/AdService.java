package br.com.promo.panfleteiro.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import br.com.promo.panfleteiro.entity.Market;
import br.com.promo.panfleteiro.entity.ProductCategory;
import br.com.promo.panfleteiro.exception.ResourceNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.promo.panfleteiro.entity.Ad;
import br.com.promo.panfleteiro.repository.AdRepository;
import br.com.promo.panfleteiro.request.AdRequest;
import br.com.promo.panfleteiro.response.AdResponse;

@Service
public class AdService {

    @Autowired
    private AdRepository adRepository;


    public Ad create(AdRequest adRequest) {
        Ad ad = new Ad();
        ad.setProductName(adRequest.getProductName());
        ad.setExpirationDate(adRequest.getExpirationDate());
        ad.setInitialDate(adRequest.getInitialDate());
        ad.setProductCategory(ProductCategory.fromName(adRequest.getProductCategory()));
        ad.setPrice(adRequest.getPrice());
        ad.setActive(adRequest.getActive());
        ad.setUrl(adRequest.getUrl());
        return adRepository.save(ad);
    }

    public List<Ad> list() {
        return adRepository.findAll();
    }
    
    public void delete(Ad ad) {
        adRepository.delete(ad);
    }

    @NotNull
    public AdResponse convertToAdResponse(Ad ad) {
        AdResponse adResponse = new AdResponse();
        adResponse.setId(ad.getId());
        adResponse.setUrl(ad.getUrl());
        adResponse.setPrice(ad.getPrice());
        adResponse.setActive(ad.getActive());
        adResponse.setProductName(ad.getProductName());
        adResponse.setProductCategory(ad.getProductCategory().getName());
        adResponse.setInitialDate(ad.getInitialDate());
        adResponse.setExpirationDate(ad.getExpirationDate());
        adResponse.setCreationDate(ad.getCreationDate());
        return adResponse;
    }

    public Ad findById(Long id) {
        return adRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ad not found with ID: " + id));
    }

    public Page<Ad> findAdsByProductName(String productName, Pageable pageable) {
        return adRepository.findAdsByProductName(productName, pageable);
    }

    public Ad saveAd(Ad ad) {
        return adRepository.save(ad);
    }

    public Page<Ad> findAdsByProductNameAndDistance(Double latitude, Double longitude, Long rangeInKm, Pageable pageable, String productName) {
        Map<String, Double> boundingBox = BoundingBoxCalculator.calculateBoundingBox(latitude, longitude, rangeInKm);

        return adRepository.findAdsByProductNameAndDistanceWithBoundingBox(
                boundingBox.get("minLat"), boundingBox.get("maxLat"),
                boundingBox.get("minLon"), boundingBox.get("maxLon"),
                latitude, longitude, rangeInKm, productName, pageable);
    }

    public List<Ad> getActiveAds() {
        return adRepository.findByActive(true);
    }


}
