package br.com.promo.panfleteiro.controller;

import java.util.List;

import br.com.promo.panfleteiro.request.LocationRequest;
import br.com.promo.panfleteiro.response.LocationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.promo.panfleteiro.service.LocationService;

@RestController
@RequestMapping("/v1/locais")
public class LocationController {

    LocationService locationService;

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    ResponseEntity<LocationResponse> create(@RequestBody LocationRequest locationRequest) {
        logger.info("Creating Location: {}", locationRequest);
        LocationResponse locationResponse = locationService.createLocation(locationRequest);
        logger.info("Created Location: {}", locationResponse.getId());
        return ResponseEntity.status(201).body(locationResponse);
    }

    @GetMapping
    ResponseEntity<List<LocationResponse>> findAll() {
        logger.info("Listing all Locations");
        return ResponseEntity.ok(locationService.findAll());
    }

    @PutMapping("/{id}")
    ResponseEntity<LocationResponse> update(@PathVariable Long id, @RequestBody LocationRequest location) {
        logger.info("Updating Location with ID: {}", id);
        return ResponseEntity.ok(locationService.updateLocation(id, location));
    }
    
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Deleting Location with ID: {}", id);
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    ResponseEntity<LocationResponse> findById(@PathVariable Long id) {
        logger.info("Looking for Location with ID: {}", id);
        return ResponseEntity.ok(locationService.getLocationResponseById(id));
    }
}
