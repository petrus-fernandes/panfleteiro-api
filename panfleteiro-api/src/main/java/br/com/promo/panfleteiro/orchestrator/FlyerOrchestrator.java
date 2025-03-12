package br.com.promo.panfleteiro.orchestrator;

import br.com.promo.panfleteiro.context.FlyerSectionContext;
import br.com.promo.panfleteiro.entity.*;
import br.com.promo.panfleteiro.request.AdRequest;
import br.com.promo.panfleteiro.request.FlyerRequest;
import br.com.promo.panfleteiro.request.FlyerSectionRequest;
import br.com.promo.panfleteiro.service.*;
import br.com.promo.panfleteiro.strategy.AdRequestStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlyerOrchestrator {

    private final AdService adService;

    private final FlyerService flyerService;

    private final MarketService marketService;

    private final FlyerSectionService flyerSectionService;

    private final ProductService productService;

    private final FlyerSectionContext context;


    public FlyerOrchestrator(AdService adService, FlyerService flyerService, MarketService marketService, FlyerSectionService flyerSectionService, ProductService productService, FlyerSectionContext context) {
        this.adService = adService;
        this.flyerService = flyerService;
        this.marketService = marketService;
        this.flyerSectionService = flyerSectionService;
        this.productService = productService;
        this.context = context;
    }

    public FlyerSection createFlyerSection(FlyerSectionRequest flyerSectionRequest) {
        List<Ad> ads = getAdsWithStrategy(flyerSectionRequest);
        Flyer flyer = flyerService.findById(flyerSectionRequest.getFlyerId());

        List<Market> markets = flyerSectionRequest.getMarketsId().stream().map(marketService::findById).collect(Collectors.toList());

        FlyerSection flyerSection = flyerSectionService.create(flyerSectionRequest);
        flyer.addFlyerSection(flyerSection);
        flyerSection.setMarkets(markets);

        ads.forEach(flyerSection::addAd);

        return flyerSectionService.save(flyerSection);
    }

    public FlyerSection updateFlyerSection(Long id, FlyerSectionRequest flyerSectionRequest) {
        FlyerSection flyerSection = flyerSectionService.findById(id);
        flyerSection.setExpirationDate(flyerSectionRequest.getExpirationDate());
        flyerSection.setInitialDate(flyerSectionRequest.getInitialDate());
        flyerSection.setActive(flyerSectionRequest.getActive());

        List<Market> marketsToRemove = new ArrayList<>(flyerSection.getMarkets());
        marketsToRemove.forEach(flyerSection::removeMarket);
        getMarketsByIds(flyerSectionRequest.getMarketsId()).forEach(flyerSection::addMarket);

        List<Ad> adsToRemove = new ArrayList<>(flyerSection.getAds());
        adsToRemove.forEach(flyerSection::removeAd);
        getAdsWithStrategy(flyerSectionRequest).forEach(flyerSection::addAd);

        return flyerSectionService.save(flyerSection);
    }

    public Flyer createFlyer(FlyerRequest flyerRequest) {
         Flyer flyer = flyerService.create(flyerRequest);
         if (flyerRequest.getMarketsId() != null && !flyerRequest.getMarketsId().isEmpty()) {
             flyerRequest.getMarketsId().stream().map(marketService::findById).forEach(flyer::addMarket);
         }
         if (flyerRequest.getFlyerSectionsId() != null && !flyerRequest.getFlyerSectionsId().isEmpty()) {
             flyerRequest.getFlyerSectionsId().stream().map(flyerSectionService::findById).forEach(flyerSection -> flyerSection.addFlyer(flyer));
         }

         return flyerService.save(flyer);
    }

    public Flyer updateFlyer(Long id, FlyerRequest flyerRequest) {
        Flyer flyer = flyerService.findById(id);
        flyer.setExpirationDate(flyerRequest.getExpirationDate());
        flyer.setInitialDate(flyerRequest.getInitialDate());
        flyer.setActive(flyerRequest.getActive());

        if (flyerRequest.getFlyerSectionsId() != null) {
            List<FlyerSection> flyerSectionsToRemove = new ArrayList<>(flyer.getFlyerSections());
            flyerSectionsToRemove.forEach(flyer::removeFlyerSection);
            getFlyerSectionsById(flyerRequest.getFlyerSectionsId()).forEach(flyer::addFlyerSection);
        }

        if (flyerRequest.getMarketsId() != null) {
            List<Market> marketsToRemove = new ArrayList<>(flyer.getMarkets());
            marketsToRemove.forEach(flyer::removeMarket);
            getMarketsByIds(flyerRequest.getMarketsId()).forEach(flyer::addMarket);
        }

        return flyerService.save(flyer);
    }

    private List<FlyerSection> getFlyerSectionsById(List<Long> flyerSectionsId) {
        return flyerSectionsId.stream().map(flyerSectionService::findById).collect(Collectors.toList());
    }

    private List<Market> getMarketsByIds(List<Long> marketsId) {
        return marketsId.stream().map(marketService::findById).collect(Collectors.toList());
    }

    public Ad createAd(AdRequest adRequest) {
        Product product = productService.findById(adRequest.getProductId());
        Ad ad = adService.create(adRequest);
        ad.setProduct(product);
        if (adRequest.getFlyerSectionId() != null) {
            FlyerSection flyerSection = flyerSectionService.findById(adRequest.getFlyerSectionId());
            flyerSection.addAd(ad);
            flyerSectionService.save(flyerSection);
        } else {
            adService.saveAd(ad);
        }
        return ad;
    }

    public Ad updateAd(Long id, AdRequest adRequest) {
        Ad ad = adService.findById(id);
        Product newProduct = productService.findById(adRequest.getProductId());
        if (ad.getProduct() != null && ad.getProduct() != newProduct) {
            ad.removeProduct();
        }
        ad.addProduct(newProduct);
        ad.setPrice(adRequest.getPrice());
        ad.setActive(adRequest.getActive());
        ad.setUrl(adRequest.getUrl());
        return adService.saveAd(ad);
    }

    public void deleteAd(Long id) {
        Ad ad = adService.findById(id);
        ad.removeFlyerSection();
        ad.removeProduct();
        adService.delete(ad);
    }

    private List<Ad> getAdsWithStrategy(FlyerSectionRequest flyerSectionRequest) {
        if (context.getStrategy(flyerSectionRequest) instanceof AdRequestStrategy) {
            return flyerSectionRequest.getAds().stream().map(this::createAd).collect(Collectors.toList());
        }
        return flyerSectionRequest.getAdsId().stream().map(adService::findById).collect(Collectors.toList());
    }
}
