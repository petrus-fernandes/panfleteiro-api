package br.com.promo.panfleteiro.context;

import br.com.promo.panfleteiro.request.FlyerSectionRequest;
import br.com.promo.panfleteiro.strategy.AdIdStrategy;
import br.com.promo.panfleteiro.strategy.AdRequestStrategy;
import br.com.promo.panfleteiro.strategy.FlyerSectionStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlyerSectionContext {

    private final List<FlyerSectionStrategy> strategies;

    public FlyerSectionContext(List<FlyerSectionStrategy> strategies) {
        this.strategies = strategies;
    }

    public FlyerSectionStrategy getStrategy(FlyerSectionRequest request) {
        if (request.getAds() != null && !request.getAds().isEmpty()) {
            return strategies.stream()
                    .filter(strategy -> strategy instanceof AdRequestStrategy)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Estratégia para AdRequest não encontrada"));
        } else if (request.getAdsId() != null && !request.getAdsId().isEmpty()) {
            return strategies.stream()
                    .filter(strategy -> strategy instanceof AdIdStrategy)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Estratégia para AdsId não encontrada"));
        } else {
            throw new IllegalArgumentException("Request inválido: nenhuma estratégia foi encontrada");
        }
    }
}

