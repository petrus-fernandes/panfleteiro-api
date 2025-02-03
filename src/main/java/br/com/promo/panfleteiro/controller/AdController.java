package br.com.promo.panfleteiro.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.promo.panfleteiro.orchestrator.FlyerOrchestrator;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.*;
import org.springframework.data.util.Streamable;
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
        flyerOrchestrator.deleteAd(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscaPorNome")
    public ResponseEntity<Page<AdResponse>> searchAdsByProductName(@RequestParam String productName, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("active", "product.name").ascending());
        Page<Ad> adsPage = adService.findAdsByProductName(productName, pageable);
        Page<AdResponse> adsResponsePage = adsPage.map(adService::convertToAdResponse);
        return ResponseEntity.ok(adsResponsePage);
    }

    @GetMapping("/buscaPorDistancia")
    public ResponseEntity<Page<AdResponse>> searchAdsByDistance(@RequestParam Double latitude, @RequestParam Double longitude, @RequestParam Long rangeInKm, @RequestParam Integer page, @RequestParam Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("active").ascending());
        Page<Ad> adsPage = adService.findAdsByDistance(latitude, longitude,rangeInKm, pageable);
        Page<AdResponse> adsResponsePage = new PageImpl<>(getAdsResponseList(latitude, longitude, rangeInKm, adsPage));
        return ResponseEntity.ok(adsResponsePage);
    }

    private List<AdResponse> getAdsResponseList(Double latitude, Double longitude, Long rangeInKm, Page<Ad> adsPage) {
        return adsPage.stream().filter(ad -> ad.getFlyerSection() != null && ad.getFlyerSection().getMarkets() != null)
                .flatMap(ad -> getAdResponseForAllMarketsInRange(rangeInKm, ad, latitude, longitude))
                .collect(Collectors.toList());
    }

    private Stream<AdResponse> getAdResponseForAllMarketsInRange(Long rangeInKm, Ad ad, Double latitude, Double longitude) {
        return ad.getFlyerSection().getMarkets().stream()
                .filter(market -> market.getLocation().calculateDistanceInKm(latitude, longitude) <= rangeInKm)
                .map(market -> adService.convertToAdResponseWithUniqueMarketAndDistance(ad, market.getId(), market.getLocation().calculateDistanceInKm(latitude, longitude)));
    }
}
