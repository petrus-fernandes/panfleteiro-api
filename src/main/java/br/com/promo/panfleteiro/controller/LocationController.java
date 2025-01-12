package br.com.promo.panfleteiro.controller;

import java.util.List;

import br.com.promo.panfleteiro.request.LocationRequest;
import br.com.promo.panfleteiro.response.LocationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.promo.panfleteiro.entity.Location;
import br.com.promo.panfleteiro.service.LocationService;

@RestController
@RequestMapping("/v1/locais")
public class LocationController {

    LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    ResponseEntity<LocationResponse> create(@RequestBody LocationRequest location) {
        return ResponseEntity.status(201).body(locationService.createLocation(location));
    }

    @GetMapping
    ResponseEntity<List<LocationResponse>> findAll() {
        return ResponseEntity.ok(locationService.findAll());
    }

    @PutMapping("/{id}")
    ResponseEntity<LocationResponse> update(@PathVariable Long id, @RequestBody LocationRequest location) {
        return ResponseEntity.ok(locationService.updateLocation(id, location));
    }
    
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    ResponseEntity<LocationResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(locationService.getLocationResponseById(id));
    }
}
