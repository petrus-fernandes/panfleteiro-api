package br.com.promo.panfleteiro.helper;

import br.com.promo.panfleteiro.entity.Ad;
import br.com.promo.panfleteiro.entity.FlyerSection;
import br.com.promo.panfleteiro.exception.ResourceNotFoundException;
import br.com.promo.panfleteiro.repository.AdRepository;
import br.com.promo.panfleteiro.repository.FlyerRepository;
import br.com.promo.panfleteiro.repository.FlyerSectionRepository;
import org.springframework.stereotype.Component;

@Component
public class AdFlyerSectionHelper {

    private final FlyerRepository flyerRepository;

    private final AdRepository adRepository;

    private final FlyerSectionRepository flyerSectionRepository;

    public AdFlyerSectionHelper(FlyerRepository flyerRepository, AdRepository adRepository, FlyerSectionRepository flyerSectionRepository) {

        this.flyerRepository = flyerRepository;
        this.adRepository = adRepository;
        this.flyerSectionRepository = flyerSectionRepository;
    }

    /**
     * Cria um Ad associado a uma FlyerSection.
     */
//    public Ad createAdForFlyerSection(AdRequest adRequest) {
//        FlyerSection flyerSection = flyerSectionService.findByIdInternal(adRequest.getFlyerSectionId());
//        Ad ad = adService.convertToAd(adRequest);
//        ad.setFlyerSection(flyerSection);
//        return adService.saveAd(ad);
//    }

    /**
     * Cria uma FlyerSection com uma lista de Ads.
     */
//    public FlyerSection createFlyerSectionWithAds(FlyerSectionRequest flyerSectionRequest) {
//        FlyerSection flyerSection = flyerSectionService.convertToFlyerSection(flyerSectionRequest);
//
//        List<Ad> ads = flyerSectionRequest.getAds().stream()
//                .map(this::createAdForFlyerSection)
//                .collect(Collectors.toList());
//
//        flyerSection.setAds(ads);
//        return flyerSectionService.saveFlyerSection(flyerSection);
//    }

    /**
     * Atualiza uma FlyerSection e seus Ads.
     */
//    public FlyerSection updateFlyerSectionWithAds(Long id, FlyerSectionRequest flyerSectionRequest) {
//        FlyerSection flyerSection = flyerSectionService.findByIdInternal(id);
//
//        List<Ad> ads = flyerSectionRequest.getAds().stream()
//                .map(this::createAdForFlyerSection)
//                .collect(Collectors.toList());
//
//        flyerSection.setAds(ads);
//        flyerSection.setExpirationDate(flyerSectionRequest.getExpirationDate());
//        flyerSection.setInitialDate(flyerSectionRequest.getInitialDate());
//        return flyerSectionService.saveFlyerSection(flyerSection);
//    }

//    public FlyerSection convertToFlyerSection(FlyerSectionRequest flyerSectionRequest) {
//        Flyer flyer = flyerRepository.findById(flyerSectionRequest.getFlyerId())
//                .orElseThrow(() -> new ResourceNotFoundException("Flyer not found with ID: " + flyerSectionRequest.getFlyerId()));
//        List<Market> markets = flyerSectionRequest.getMarketIds().stream().map(marketService::findById).toList();
//        List<Ad> ads = flyerSectionRequest.getAds().stream().map(adService::convertToAd).toList();
//        return new FlyerSection(flyer, flyerSectionRequest.getExpirationDate(), flyerSectionRequest.getInitialDate());
//    }

    public Ad findAdById(Long id) {
        return adRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ad not found with ID: " + id));
    }





    public FlyerSection findFlyerSectionById(Long flyerSectionId) {
        return flyerSectionRepository.findById(flyerSectionId).orElseThrow(() -> new ResourceNotFoundException("FlyerSection not found with ID: " + flyerSectionId));
    }
}

