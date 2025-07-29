package br.com.promo.panfleteiro.helper;

import br.com.promo.panfleteiro.entity.Location;
import br.com.promo.panfleteiro.entity.Market;
import br.com.promo.panfleteiro.repository.LocationRepository;
import br.com.promo.panfleteiro.response.LocationResponse;
import br.com.promo.panfleteiro.response.MarketResponse;
import br.com.promo.panfleteiro.integration.service.GeocodingApiService;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class MarketLocationHelper {

    private final GeocodingApiService geocodingApiService;

    private final LocationRepository locationRepository;

    public MarketLocationHelper(GeocodingApiService geocodingApiService, LocationRepository locationRepository) {
        this.geocodingApiService = geocodingApiService;
        this.locationRepository = locationRepository;
    }

    public LocationResponse convertLocationToResponse(Location location) {
        return new LocationResponse(
                location.getId(),
                location.getLatitude(),
                location.getLongitude(),
                location.getAddress(),
                location.getActive()
        );
    }

    public MarketResponse convertMarketToResponse(Market market) {
        return new MarketResponse(
                market.getId(),
                market.getName(),
                market.getLocation() != null ? convertLocationToResponse(market.getLocation()) : null,
                market.getExternalCode(),
                market.getMarketChain() != null ? market.getMarketChain().stream().map(Market::getId).toList() : null,
                market.isHeadQuarters()
        );
    }

    @Transactional
    public Optional<Location> findLocationWithAddress(String address) {
        Location location = geocodingApiService.getLocationWithAddress(address);
        return locationRepository.findByAddress(location.getAddress());
    }

    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }

    public Location createLocationWithAddress(String address) {
        Location location = geocodingApiService.getLocationWithAddress(address);
        return saveLocation(location);
    }

    public Location getLocationWithAddress(String address) {
        return geocodingApiService.getLocationWithAddress(address);
    }
}

