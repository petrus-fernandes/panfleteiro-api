package br.com.promo.panfleteiro.strategy;

import br.com.promo.panfleteiro.entity.Ad;
import br.com.promo.panfleteiro.request.FlyerSectionRequest;
import br.com.promo.panfleteiro.service.AdService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdRequestStrategy implements FlyerSectionStrategy {

    private final AdService adService;

    public AdRequestStrategy(AdService adService) {
        this.adService = adService;
    }

}

