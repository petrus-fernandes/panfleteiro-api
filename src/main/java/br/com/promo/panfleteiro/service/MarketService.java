package br.com.promo.panfleteiro.service;

import java.util.List;
import java.util.Optional;

import br.com.promo.panfleteiro.helper.MarketLocationHelper;
import br.com.promo.panfleteiro.exception.ResourceNotFoundException;
import org.jetbrains.annotations.NotNull;
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
        applyValidations(marketRequest);

        Optional<Location> location = marketLocationHelper.findLocationWithAddress(marketRequest.getAddress());
        Location persistedLocation = location.orElseGet(() -> (marketLocationHelper.createLocationWithAddress(marketRequest.getAddress())));
        persistedLocation.setActive(true);

        Market market = new Market(marketRequest.getName(),persistedLocation);
        market.setExternalCode(marketRequest.getExternalCode());
        market.setHeadQuarters(marketRequest.isHeadQuarters());

        if (marketRequest.isHeadQuarters() && marketRequest.getMarketsId() != null && !marketRequest.getMarketsId().isEmpty()) {
            market.setMarketChain(marketRepository.findAllById(marketRequest.getMarketsId()));
        }

        return marketLocationHelper.convertMarketToResponse(marketRepository.save(market));
    }

    private void applyValidations(MarketRequest marketRequest) {
        validateExternalCode(marketRequest.getExternalCode());

        if (marketRequest.getMarketsId() != null && !marketRequest.getMarketsId().isEmpty()) {
            validateMarketsId(marketRequest.getMarketsId());
        }
    }

    private void validateMarketsId(List<Long> marketsId) {
        marketsId.forEach(marketId -> {
            if (marketRepository.findMarketContainingMarketId(marketId).isPresent()) {
                throw new IllegalArgumentException("Market with ID " + marketId + " is already associated with another market.");
            }
        });
    }

    private void validateExternalCode(String externalCode) {
        if (externalCode == null || externalCode.isEmpty()) return;

        if (marketRepository.existsByExternalCode(externalCode)) {
            throw new IllegalArgumentException("Market with external code " + externalCode + " already exists.");
        }
    }

    public List<MarketResponse> findAll() {
        return marketRepository.findAll().stream().map(marketLocationHelper::convertMarketToResponse).toList();
    }

    public Market findById(Long id) {
        return marketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Market not found with ID: " + id));

    }

    public MarketResponse update(Long id, MarketRequest marketRequest) {
        validateExternalCode(marketRequest.getExternalCode());
        Market market = marketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Market not found with ID: " + id));
        Optional<Location> locationOptional = marketLocationHelper.findLocationWithAddress(marketRequest.getAddress());
        Location location = locationOptional.orElseGet(() -> marketLocationHelper.createLocationWithAddress(marketRequest.getAddress()));

        market.setName(marketRequest.getName());
        market.setLocation(location);
        market.setExternalCode(marketRequest.getExternalCode());

        if (marketRequest.isHeadQuarters() && marketRequest.getMarketsId() != null && !marketRequest.getMarketsId().isEmpty()) {
            market.setMarketChain(marketRepository.findAllById(marketRequest.getMarketsId()));
        }

        return marketLocationHelper.convertMarketToResponse(marketRepository.save(market));
    }

    public void delete(Long id) {
        Market market = marketRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Market not found with ID: " + id));
        marketRepository.delete(market);
    }

    public List<MarketResponse> findByName(String name) {
        return marketRepository.findByNameContainingIgnoreCase(name).stream().map(marketLocationHelper::convertMarketToResponse).toList();
    }

    public Market findByExternalCode(String marketExternalCode) {
        return marketRepository.findByExternalCode(marketExternalCode);
    }

    public Market findMarketContainingMarketId(Long marketId) {
        return marketRepository.findMarketContainingMarketId(marketId).orElse(null);
    }

    @NotNull
    public MarketResponse convertToMarketResponseWithDistance(Double latitude, Double longitude, Market market) {
        MarketResponse response = marketLocationHelper.convertMarketToResponse(market);
        response.setDistance(market.getLocation().calculateDistanceInKm(latitude, longitude));
        return response;
    }
}
