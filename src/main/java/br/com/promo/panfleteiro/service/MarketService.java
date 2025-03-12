package br.com.promo.panfleteiro.service;

import java.util.List;
import java.util.Optional;

import br.com.promo.panfleteiro.helper.MarketLocationHelper;
import br.com.promo.panfleteiro.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import br.com.promo.panfleteiro.entity.Location;
import br.com.promo.panfleteiro.entity.Market;
import br.com.promo.panfleteiro.repository.MarketRepository;
import br.com.promo.panfleteiro.request.MarketRequest;
import br.com.promo.panfleteiro.response.MarketResponse;

@Service
public class MarketService {

    private final MarketRepository marketRepository;
    private final MarketLocationHelper marketLocationHelper;

    public MarketService(MarketRepository marketRepository, MarketLocationHelper marketLocationHelper) {
        this.marketRepository = marketRepository;
        this.marketLocationHelper = marketLocationHelper;
    }

    public MarketResponse create(MarketRequest marketRequest) {
        Optional<Location> location = marketLocationHelper.findLocationWithAddress(marketRequest.getAddress());
        Location persistedLocation = location.orElseGet(() -> (marketLocationHelper.createLocationWithAddress(marketRequest.getAddress())));
        persistedLocation.setActive(true);
        Market market = marketRepository.save(new Market(marketRequest.getName(),persistedLocation));
        return marketLocationHelper.convertMarketToResponse(market);
    }

    public List<MarketResponse> findAll() {
        return marketRepository.findAll().stream().map(marketLocationHelper::convertMarketToResponse).toList();
    }

    public Market findById(Long id) {
        return marketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Market not found with ID: " + id));

    }

    public MarketResponse update(Long id, MarketRequest marketRequest) {
        Market market = marketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Market not found with ID: " + id));
        Optional<Location> locationOptional = marketLocationHelper.findLocationWithAddress(marketRequest.getAddress());
        Location location = locationOptional.orElseGet(() -> marketLocationHelper.createLocationWithAddress(marketRequest.getAddress()));

        market.setName(marketRequest.getName());
        market.setLocation(location);

        return marketLocationHelper.convertMarketToResponse(marketRepository.save(market));
    }

    public void delete(Long id) {
        Market market = marketRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Market not found with ID: " + id));
        marketRepository.delete(market);
    }

    public MarketResponse getMarketResponseById(Long id){
        return marketLocationHelper.convertMarketToResponse(findById(id));
    }

    public List<MarketResponse> findByName(String name) {
        return marketRepository.findByNameContainingIgnoreCase(name).stream().map(marketLocationHelper::convertMarketToResponse).toList();
    }
}
