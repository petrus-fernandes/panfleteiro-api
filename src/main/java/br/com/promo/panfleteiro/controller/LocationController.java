package br.com.promo.panfleteiro.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.promo.panfleteiro.entity.Location;
import br.com.promo.panfleteiro.service.LocationService;

@RestController
@RequestMapping("/locais")
public class LocationController {

    LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    List<Location> create(Location location) {
        return locationService.create(location);
    }

    @GetMapping
    List<Location> list() {
        return locationService.list();
    }

    @PutMapping
    List<Location> update(Location location) {
        return locationService.update(location);
    }
    
    @DeleteMapping("{id}")
    List<Location> delete(@PathVariable Long id) {
        return locationService.delete(id);
    }
}
