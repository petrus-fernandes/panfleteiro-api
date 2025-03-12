package br.com.promo.panfleteiro.strategy;

import br.com.promo.panfleteiro.service.AdService;
import org.springframework.stereotype.Component;

@Component
public class AdRequestStrategy implements FlyerSectionStrategy {

    private final AdService adService;

    public AdRequestStrategy(AdService adService) {
        this.adService = adService;
    }

}

