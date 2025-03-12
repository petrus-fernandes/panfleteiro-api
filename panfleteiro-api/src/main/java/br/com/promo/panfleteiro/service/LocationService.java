package br.com.promo.panfleteiro.service;

import java.util.List;
import java.util.Optional;

import br.com.promo.panfleteiro.helper.MarketLocationHelper;
import br.com.promo.panfleteiro.exception.ResourceAlreadyExistsException;
import br.com.promo.panfleteiro.exception.ResourceNotFoundException;
import br.com.promo.panfleteiro.request.LocationRequest;
import org.springframework.stereotype.Service;

import br.com.promo.panfleteiro.entity.Location;
import br.com.promo.panfleteiro.repository.LocationRepository;
import br.com.promo.panfleteiro.response.LocationResponse;

@Service
public class LocationService {

    private LocationRepository locationRepository;
    private MarketLocationHelper marketLocationHelper;

    public LocationService(LocationRepository locationRepository, MarketLocationHelper marketLocationHelper) {
        this.locationRepository = locationRepository;
        this.marketLocationHelper = marketLocationHelper;
    }
    
    public LocationResponse createLocation(LocationRequest locationRequest) {
        Location location = convertToLocation(locationRequest);
        locationRepository.findByAddress(location.getAddress())
                .ifPresent(locationFind -> {
                    throw new ResourceAlreadyExistsException(locationFind.getClass().getSimpleName(), locationFind.getId().toString());
                });
        return marketLocationHelper.convertLocationToResponse(locationRepository.save(location));
    }

    public List<LocationResponse> findAll() {
        return locationRepository.findAll().stream().map(marketLocationHelper::convertLocationToResponse).toList();
    }
    
    public LocationResponse updateLocation(Long id, LocationRequest locationRequest) {
        Location persistedLocation = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with ID: " + id));

        Location newLocation = marketLocationHelper.getLocationWithAddress(locationRequest.getAddress());
        persistedLocation.setAddress(newLocation.getAddress());
        persistedLocation.setLatitude(newLocation.getLatitude());
        persistedLocation.setLongitude(newLocation.getLongitude());
        persistedLocation.setActive(locationRequest.getActive());

        return marketLocationHelper.convertLocationToResponse(locationRepository.save(persistedLocation));

    }
    
    public void deleteLocation(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Location not found with ID: " + id);
        }
        locationRepository.deleteById(id);
    }

    public Location convertToLocation(LocationRequest locationRequest) {
        Optional<Location> location = marketLocationHelper.findLocationWithAddress(locationRequest.getAddress());
        location.ifPresent(persistedLocation -> persistedLocation.setActive(locationRequest.getActive()));
        return location.orElseGet(() -> {
            Location newLocation = marketLocationHelper.getLocationWithAddress(locationRequest.getAddress());
            newLocation.setActive(locationRequest.getActive());
            return newLocation;
        });
    }

    public LocationResponse getLocationResponseById(Long id) {
        Location location = locationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Location not found with ID: " + id));
        return marketLocationHelper.convertLocationToResponse(location);
    }
}