package br.com.promo.panfleteiro.service;

import br.com.promo.panfleteiro.entity.Ad;
import br.com.promo.panfleteiro.entity.FlyerSection;
import br.com.promo.panfleteiro.entity.Market;
import br.com.promo.panfleteiro.exception.ResourceNotFoundException;
import br.com.promo.panfleteiro.repository.FlyerSectionRepository;
import br.com.promo.panfleteiro.request.FlyerSectionRequest;
import br.com.promo.panfleteiro.response.FlyerSectionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlyerSectionService {

    private final FlyerSectionRepository flyerSectionRepository;

    public FlyerSectionService(FlyerSectionRepository flyerSectionRepository) {
        this.flyerSectionRepository = flyerSectionRepository;
    }

    public FlyerSection create(FlyerSectionRequest flyerSectionRequest) {
        FlyerSection flyerSection = new FlyerSection();
        flyerSection.setExpirationDate(flyerSectionRequest.getExpirationDate());
        flyerSection.setInitialDate(flyerSectionRequest.getInitialDate());
        flyerSection.setActive(flyerSectionRequest.getActive());
        return flyerSectionRepository.save(flyerSection);
    }

    public List<FlyerSection> findAll() {
        return flyerSectionRepository.findAll();
    }

    public FlyerSection findById(Long id) {
        return flyerSectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FlyerSection not found with ID: " + id));
    }

    public FlyerSection update(Long id, FlyerSectionRequest flyerSectionRequest) {
        FlyerSection flyerSection = flyerSectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FlyerSection not found with ID: " + id));
        flyerSection.setExpirationDate(flyerSectionRequest.getExpirationDate());
        flyerSection.setInitialDate(flyerSectionRequest.getInitialDate());
        flyerSection.setActive(flyerSectionRequest.getActive());

        return flyerSectionRepository.save(flyerSection);
    }

    public void delete(Long id) {
        FlyerSection section = flyerSectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FlyerSection not found with ID: " + id));
        flyerSectionRepository.delete(section);
    }

    public FlyerSectionResponse convertToResponse(FlyerSection flyerSection) {
        FlyerSectionResponse flyerSectionResponse = new FlyerSectionResponse();
        flyerSectionResponse.setId(flyerSection.getId());
        if(flyerSection.getFlyer() != null) {
            flyerSectionResponse.setFlyerId(flyerSection.getFlyer().getId());
        }
        flyerSectionResponse.setMarketsId(flyerSection.getMarkets().stream().map(Market::getId).toList());
        flyerSectionResponse.setExpirationDate(flyerSection.getExpirationDate());
        flyerSectionResponse.setInitialDate(flyerSection.getInitialDate());
        flyerSectionResponse.setAdsId(flyerSection.getAds().stream().map(Ad::getId).toList());
        flyerSectionResponse.setActive(flyerSection.getActive());
        return flyerSectionResponse;
    }

    public FlyerSection save(FlyerSection flyerSection) {
        return flyerSectionRepository.save(flyerSection);
    }
}
