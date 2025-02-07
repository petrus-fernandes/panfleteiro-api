package br.com.promo.panfleteiro.service;

import br.com.promo.panfleteiro.entity.Flyer;
import br.com.promo.panfleteiro.entity.FlyerSection;
import br.com.promo.panfleteiro.entity.Market;
import br.com.promo.panfleteiro.exception.ResourceNotFoundException;
import br.com.promo.panfleteiro.repository.FlyerRepository;
import br.com.promo.panfleteiro.repository.FlyerSectionRepository;
import br.com.promo.panfleteiro.repository.MarketRepository;
import br.com.promo.panfleteiro.request.FlyerRequest;
import br.com.promo.panfleteiro.response.FlyerResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlyerService {

    private final FlyerRepository flyerRepository;
    private final MarketRepository marketRepository;

    private final FlyerSectionRepository flyerSectionRepository;

    public FlyerService(FlyerRepository flyerRepository, MarketRepository marketRepository, FlyerSectionRepository flyerSectionRepository) {
        this.flyerRepository = flyerRepository;
        this.marketRepository = marketRepository;
        this.flyerSectionRepository = flyerSectionRepository;
    }

    public Flyer create(FlyerRequest flyerRequest) {
        Flyer flyer = new Flyer();
        flyer.setExpirationDate(flyerRequest.getExpirationDate());
        flyer.setInitialDate(flyerRequest.getInitialDate());
        flyer.setActive(flyerRequest.getActive());
        return flyerRepository.save(flyer);
    }

    public List<Flyer> findAll() {
        return flyerRepository.findAll();
    }

    public Flyer findById(Long id) {
        return flyerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flyer not found with ID: " + id));
    }

    public Flyer update(Long id, FlyerRequest flyerRequest) {
        Flyer flyer = flyerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flyer not found with ID: " + id));
        flyer.setExpirationDate(flyerRequest.getExpirationDate());
        flyer.setInitialDate(flyerRequest.getInitialDate());
        flyer.setActive(flyerRequest.getActive());
        return flyerRepository.save(flyer);
    }

    public void delete(Long id) {
        Flyer flyer = flyerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flyer not found with ID: " + id));
        flyerRepository.delete(flyer);
    }
    public FlyerResponse convertToResponse(Flyer flyer) {
        List<Long> marketsId = flyer.getMarkets().stream()
                .map(Market::getId)
                .collect(Collectors.toList());

        List<Long> flyerSectionsId = flyer.getFlyerSections().stream()
                .map(FlyerSection::getId)
                .collect(Collectors.toList());

        return new FlyerResponse(flyer.getId(), flyer.getExpirationDate(), flyer.getInitialDate(), marketsId, flyer.getActive(), flyerSectionsId);
    }

    public Flyer save(Flyer flyer) {
        return flyerRepository.save(flyer);
    }

    public List<Flyer> getActiveFlyers() {
        return flyerRepository.findByActive(true);
    }
}

