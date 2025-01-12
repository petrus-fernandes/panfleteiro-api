package br.com.promo.panfleteiro.controller;

import java.util.List;

import br.com.promo.panfleteiro.orchestrator.FlyerOrchestrator;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.promo.panfleteiro.entity.Ad;
import br.com.promo.panfleteiro.request.AdRequest;
import br.com.promo.panfleteiro.response.AdResponse;
import br.com.promo.panfleteiro.service.AdService;

@RestController
@RequestMapping("v1/anuncios")
public class AdController {

    private final AdService adService;

    private final FlyerOrchestrator flyerOrchestrator;

    public AdController(AdService adService, FlyerOrchestrator flyerOrchestrator) {
        this.adService = adService;
        this.flyerOrchestrator = flyerOrchestrator;
    }

    @PostMapping
    public ResponseEntity<AdResponse> create(@Valid @RequestBody AdRequest adRequest) {
        return ResponseEntity.status(201).body(adService.convertToAdResponse(flyerOrchestrator.createAd(adRequest)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(adService.convertToAdResponse(adService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<List<AdResponse>> list() {
        return ResponseEntity.ok(adService.list());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdResponse> update(@PathVariable Long id, @RequestBody AdRequest adRequest) {
        return ResponseEntity.ok(adService.convertToAdResponse(flyerOrchestrator.updateAd(id, adRequest)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Ad>> searchAdsByProductName(@RequestParam String productName, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        Page<Ad> adsPage = adService.findAdsByProductName(productName, pageable);
        return ResponseEntity.ok(adsPage);
    }
}
