package br.com.promo.panfleteiro.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.promo.panfleteiro.entity.Location;
import br.com.promo.panfleteiro.repository.LocationRepository;

@Service
public class LocationService {

    private LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }
    
    public List<Location> create(Location location) {
        locationRepository.save(location);
        return list();
    }

    public List<Location> list() {
        Sort.by("address").ascending();
        return locationRepository.findAll();
    }
    
    public List<Location> update(Location location) {
        locationRepository.save(location);
        return list();
    }
    
    public List<Location> delete(Long id) {
        locationRepository.deleteById(id);
        return list();
    }
}
