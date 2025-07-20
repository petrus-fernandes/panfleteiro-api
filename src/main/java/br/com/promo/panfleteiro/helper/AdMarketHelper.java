package br.com.promo.panfleteiro.helper;

import br.com.promo.panfleteiro.entity.Ad;
import br.com.promo.panfleteiro.entity.Market;
import br.com.promo.panfleteiro.entity.ProductCategory;
import br.com.promo.panfleteiro.request.AdLotRequest;
import br.com.promo.panfleteiro.request.AdRequest;
import br.com.promo.panfleteiro.response.AdResponse;
import br.com.promo.panfleteiro.response.MarketResponse;
import br.com.promo.panfleteiro.service.AdService;
import br.com.promo.panfleteiro.service.MarketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AdMarketHelper {

    private static final Logger logger = LoggerFactory.getLogger(AdMarketHelper.class);

    @Autowired
    AdService adService;

    @Autowired
    MarketLocationHelper marketLocationHelper;

    @Autowired
    MarketService marketService;

    public List<AdResponse> listAdsResponseWithMarket() {
        return adService.list().stream().flatMap(ad -> ad.getMarkets().stream().map(market -> {
                AdResponse response = adService.convertToAdResponse(ad);
                response.setMarket(marketLocationHelper.convertMarketToResponse(market));
                return response;
        })).collect(Collectors.toList());
    }

    public Ad createAdWithMarket(AdRequest adRequest) {
        Ad ad = adService.create(adRequest);
        ad.setMarkets(adRequest.getMarketsId().stream().map(marketId -> marketService.findById(marketId)).collect(Collectors.toList()));
        ad.getMarkets().forEach(market -> market.addAd(ad));
        return adService.saveAd(ad);
    }

    public List<AdResponse> convertToAdsResponse(Ad ad) {
        return ad.getMarkets().stream()
                .map(market -> {
                    AdResponse response = adService.convertToAdResponse(ad);
                    response.setMarket(marketLocationHelper.convertMarketToResponse(market));
                    return response;
                }).collect(Collectors.toList());
    }

    public List<AdResponse> createAd(AdRequest adRequest) {
        Ad ad = this.createAdWithMarket(adRequest);
        return this.convertToAdsResponse(ad);
    }

    public List<AdResponse> updateAd(Long id, AdRequest adRequest) {
        Ad ad = updateAdWithMarket(id, adRequest);
        return this.convertToAdsResponse(ad);
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

    private boolean isExpirated(Date expirationDate) {
        return expirationDate != null && expirationDate.before(new Date());
    }

    public AdResponse convertToAdResponseWithUniqueMarketAndDistance(Ad ad, Market market, Double distance) {
        AdResponse adResponse = adService.convertToAdResponse(ad);
        adResponse.setDistance(distance);
        if (market != null) {
            adResponse.setMarket(marketLocationHelper.convertMarketToResponse(market));
        }
        return adResponse;
    }

    public Stream<AdResponse> getAdResponseStreamForAllMarketsInRange(Long rangeInKm, Ad ad, Double latitude, Double longitude) {
        return ad.getMarkets().stream()
                .filter(market -> market.getLocation().calculateDistanceInKm(latitude, longitude) <= rangeInKm)
                .map(market -> convertToAdResponseWithUniqueMarketAndDistance(ad, market, market.getLocation().calculateDistanceInKm(latitude, longitude)));
    }

    public List<AdResponse> createAdLot(AdLotRequest adLotRequest) {
        return adLotRequest.getAds().stream().flatMap(adRequest -> {
            if (adRequest.getMarketsId() == null || adRequest.getMarketsId().isEmpty()) {
                adRequest.setMarketsId(getMarketsId(adLotRequest));
            }

            if (adRequest.getInitialDate() == null && adLotRequest.getInitialDate() != null) {
                adRequest.setInitialDate(adLotRequest.getInitialDate());
            }

            if (adRequest.getExpirationDate() == null && adLotRequest.getExpirationDate() != null) {
                adRequest.setExpirationDate(adLotRequest.getExpirationDate());
            }

            if (adRequest.getUrl() == null || adRequest.getUrl().isEmpty()) {
                adRequest.setUrl(getUrl(adLotRequest));
            }

            Ad ad = this.createAdWithMarket(adRequest);
            return convertToAdsResponse(ad).stream();
        }).collect(Collectors.toList());
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
}
