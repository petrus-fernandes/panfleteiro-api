package br.com.promo.panfleteiro.helper;

import br.com.promo.panfleteiro.ad.AdSearchRequest;
import br.com.promo.panfleteiro.entity.Ad;
import br.com.promo.panfleteiro.entity.Location;
import br.com.promo.panfleteiro.entity.Market;
import br.com.promo.panfleteiro.entity.ProductCategory;
import br.com.promo.panfleteiro.exception.ResourceNotFoundException;
import br.com.promo.panfleteiro.integration.service.GeocodingApiService;
import br.com.promo.panfleteiro.request.AdLotRequest;
import br.com.promo.panfleteiro.request.AdRequest;
import br.com.promo.panfleteiro.response.AdResponse;
import br.com.promo.panfleteiro.response.MarketResponse;
import br.com.promo.panfleteiro.service.AdService;
import br.com.promo.panfleteiro.service.BoundingBoxCalculator;
import br.com.promo.panfleteiro.service.MarketService;
import br.com.promo.panfleteiro.util.HaversineCalculator;
import br.com.promo.panfleteiro.util.ProductNameNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AdMarketHelper {

    private static final Logger logger = LoggerFactory.getLogger(AdMarketHelper.class);

    @Autowired
    AdService adService;

    @Autowired
    MarketLocationHelper marketLocationHelper;

    @Autowired
    MarketService marketService;

    @Autowired
    private GeocodingApiService geocodingApiService;

    public List<AdResponse> listAdsResponseWithMarket() {
        return adService.list().stream().map(ad -> {
            AdResponse response = adService.convertToAdResponse(ad);
            response.setMarkets(ad.getMarkets().stream().map(market -> marketLocationHelper.convertMarketToResponse(market)).collect(Collectors.toList()));
            return response;
        }).collect(Collectors.toList());
    }

    public Ad createAdWithMarket(AdRequest adRequest) {
        Ad ad = adService.create(adRequest);
        ad.setMarkets(adRequest.getMarketsId().stream().map(marketId -> marketService.findById(marketId)).collect(Collectors.toList()));
        ad.getMarkets().forEach(market -> market.addAd(ad));
        return adService.saveAd(ad);
    }

    public AdResponse convertToAdResponse(Ad ad) {
        AdResponse response = adService.convertToAdResponse(ad);
        response.setMarkets(ad.getMarkets().stream().map(market -> marketLocationHelper.convertMarketToResponse(market)).collect(Collectors.toList()));
        return response;
    }

    public AdResponse convertToAdResponseWithMarkets(Ad ad, Double latitude, Double longitude) {
        AdResponse adResponse = adService.convertToAdResponse(ad);
        adResponse.setMarkets(ad.getMarkets().stream().map(market -> marketService.convertToMarketResponseWithDistance(latitude, longitude, market)).collect(Collectors.toList()));
        return adResponse;
    }

    public AdResponse createAd(AdRequest adRequest) {
        Ad ad = this.createAdWithMarket(adRequest);
        return this.convertToAdResponse(ad);
    }

    public AdResponse updateAd(Long id, AdRequest adRequest) {
        Ad ad = updateAdWithMarket(id, adRequest);
        return this.convertToAdResponse(ad);
    }

    private Ad updateAdWithMarket(Long id, AdRequest adRequest) {
        Ad ad = adService.findById(id);
        ad.setProductName(adRequest.getProductName());
        ad.setProductCategory(ProductCategory.fromName(adRequest.getProductCategory()));
        ad.setUrl(adRequest.getUrl());
        ad.setActive(adRequest.getActive());
        ad.setPrice(adRequest.getPrice());
        ad.setMarkets(adRequest.getMarketsId().stream().map(marketId -> marketService.findById(marketId)).collect(Collectors.toList()));
        updateMarkets(adRequest, ad);
        ad.setInitialDate(adRequest.getInitialDate());
        ad.setExpirationDate(adRequest.getExpirationDate());
        ad = adService.saveAd(ad);
        return ad;
    }

    private void updateMarkets(AdRequest adRequest, Ad ad) {
        ad.getMarkets().forEach(m -> m.removeAd(ad));
        ad.setMarkets(adRequest.getMarketsId().stream().map(marketId -> marketService.findById(marketId)).collect(Collectors.toList()));
        ad.getMarkets().forEach(m -> m.addAd(ad));
    }

    public void deleteAd(Long id) {
        Ad ad = adService.findById(id);
        ad.getMarkets().forEach(market -> market.removeAd(ad));
        adService.delete(ad);
    }

    public void deactivateEntitiesByExpiratedDate() {
        adService.getActiveAds().stream().filter(ad -> isExpirated(ad.getExpirationDate())).forEach(ad -> {
            ad.setActive(false);
            adService.saveAd(ad);
            logger.info("Ad: " + ad.getId() + " expirated.");
        });
    }

    private boolean isExpirated(LocalDate expirationDate) {
        return expirationDate != null && expirationDate.isBefore(new Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
    }

    public List<AdResponse> createAdLot(AdLotRequest adLotRequest) {
        return adLotRequest.getAds().stream().map(adRequest -> {
            if (adRequest.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("O preço do produto " + adRequest.getProductName() + " não pode ser menor ou igual a zero");
            }

            if (adRequest.getMarketsId() == null || adRequest.getMarketsId().isEmpty()) {
                adRequest.setMarketsId(getMarketsId(adLotRequest));
            }

            if (adRequest.getInitialDate() == null && adLotRequest.getInitialDate() != null) {
                adRequest.setInitialDate(adLotRequest.getInitialDate());
            } else if (adRequest.getInitialDate() == null && adLotRequest.getInitialDate() == null) {
                throw new RuntimeException("Data de inicio não informada para o anúncio: " + adRequest.getProductName());
            }

            if (adRequest.getExpirationDate() == null && adLotRequest.getExpirationDate() != null) {
                adRequest.setExpirationDate(adLotRequest.getExpirationDate());
            } else if (adRequest.getExpirationDate() == null && adLotRequest.getExpirationDate() == null) {
                throw new RuntimeException("Data de expiração não informada para o anúncio: " + adRequest.getProductName());
            }

            if (adRequest.getUrl() == null || adRequest.getUrl().isEmpty()) {
                adRequest.setUrl(getUrl(adLotRequest));
            }

            normalizeProductName(adRequest);
            Ad ad = this.createAdWithMarket(adRequest);
            return convertToAdResponse(ad);
        }).collect(Collectors.toList());
    }

    private static void normalizeProductName(AdRequest adRequest) {
        adRequest.setProductName(ProductNameNormalizer.normalize(adRequest.getProductName()));
    }

    private static String getUrl(AdLotRequest adLotRequest) {
        if (adLotRequest.getUrl() != null || !adLotRequest.getUrl().isEmpty()) {
            return adLotRequest.getUrl();
        } else {
            throw new RuntimeException("Url not informed, please inform url.");
        }
    }

    private List<Long> getMarketsId(AdLotRequest adLotRequest) {
        if (adLotRequest.getMarketsId() != null && !adLotRequest.getMarketsId().isEmpty()) {
            return adLotRequest.getMarketsId();

        } else if (adLotRequest.getMarketExternalCode() != null && !adLotRequest.getMarketExternalCode().isEmpty()) {
            return getMarketsIdByExternalCode(adLotRequest.getMarketExternalCode());

        } else {
            throw new RuntimeException("Market not informed, please inform marketsId or marketExternalCode.");
        }
    }

    private List<Long> getMarketsIdByExternalCode(String marketExternalCode) {
        Market market = marketService.findByExternalCode(marketExternalCode);
        if (market == null) {
            throw new RuntimeException("Market not found by external code: " + marketExternalCode);
        }
        return market.getMarketChain().stream().map(Market::getId).collect(Collectors.toList());
    }

    public MarketResponse getMarketResponseById(Long id) {
        Market market = marketService.findById(id);
        return this.convertToMarketWithAdsResponse(market);
    }

    private MarketResponse convertToMarketWithAdsResponse(Market market) {
        MarketResponse marketResponse = marketLocationHelper.convertMarketToResponse(market);
        market.getAds().forEach(ad -> marketResponse.getAds().add(adService.convertToAdResponse(ad)));
        return marketResponse;
    }


    public void orderMarketAdsByDistanceInRange(List<MarketResponse> markets, Long rangeInKm) {
        markets.sort(Comparator.comparingDouble(MarketResponse::getDistance));
        markets.removeIf(market -> market.getDistance() > rangeInKm);
    }

    private List<AdResponse> getAdsResponseListSorted(Double latitude, Double longitude, Long rangeInKm, List<Ad> adsPage) {
        List<AdResponse> adsResponseList = adsPage.stream().map(ad -> {
            AdResponse adResponse = this.convertToAdResponseWithMarkets(ad, latitude, longitude);
            adResponse.orderMarketsByDistanceInRange(rangeInKm);
            return adResponse;
        }).collect(Collectors.toList());


        Comparator<AdResponse> comparator =
                Comparator.comparing(AdResponse::getActive, Comparator.reverseOrder())
                        .thenComparing(AdResponse::getCreationDate, Comparator.reverseOrder())
                        .thenComparing(AdResponse::getNearestMarketDistance)
                        .thenComparing(AdResponse::getProductName, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(AdResponse::getExpirationDate);

        return adsResponseList.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public Page<AdResponse> searchAds(AdSearchRequest request, Pageable pageable) {
        if (request.getCep() != null) {
            Location location = geocodingApiService.getLocationWithAddress(request.getCep());
            request.setLatitude(location.getLatitude());
            request.setLongitude(location.getLongitude());
        }

        Page<Ad> adsPage = adService.search(request, pageable);

        if (request.getLatitude() == null || request.getLongitude() == null || request.getRangeInKm() == null) {
            return adsPage.map(this::convertToAdResponse);
        }

        double latitude = request.getLatitude();
        double longitude = request.getLongitude();
        Long range = request.getRangeInKm();

        List<Ad> filteredAds = adsPage.getContent().stream()
                .filter(ad -> anyMarketInRangeWithHarversine(ad.getMarkets(), latitude, longitude, range))
                .toList();

        List<AdResponse> responseList = getAdsResponseListSorted(latitude, longitude, range, filteredAds);

        return new PageImpl<>(responseList, pageable, filteredAds.size());
    }

    private boolean anyMarketInRangeWithHarversine(
            List<Market> markets,
            double baseLat,
            double baseLon,
            double rangeInKm
    ) {
        return markets.stream().anyMatch(market -> {
                    double distance = HaversineCalculator.distanceInKm(
                            baseLat,
                            baseLon,
                            market.getLocation().getLatitude(),
                            market.getLocation().getLongitude()
                    );
                    return distance <= rangeInKm;
        });
    }
}
