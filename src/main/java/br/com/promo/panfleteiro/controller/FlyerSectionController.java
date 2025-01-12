package br.com.promo.panfleteiro.controller;

import br.com.promo.panfleteiro.entity.FlyerSection;
import br.com.promo.panfleteiro.orchestrator.FlyerOrchestrator;
import br.com.promo.panfleteiro.request.FlyerSectionRequest;
import br.com.promo.panfleteiro.response.FlyerSectionResponse;
import br.com.promo.panfleteiro.service.FlyerSectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/panfleto-secoes")
public class FlyerSectionController {

    private final FlyerSectionService flyerSectionService;

    private final FlyerOrchestrator flyerOrchestrator;

    public FlyerSectionController(FlyerSectionService flyerSectionService, FlyerOrchestrator flyerOrchestrator) {
        this.flyerSectionService = flyerSectionService;
        this.flyerOrchestrator = flyerOrchestrator;
    }

    @PostMapping
    public ResponseEntity<FlyerSectionResponse> create(@RequestBody FlyerSectionRequest flyerSectionRequest) {
        FlyerSection flyerSection = flyerOrchestrator.createFlyerSection(flyerSectionRequest);
        return ResponseEntity.ok(flyerSectionService.convertToResponse(flyerSection));
    }

    @GetMapping
    public ResponseEntity<List<FlyerSectionResponse>> findAll() {
        return ResponseEntity.ok(flyerSectionService.findAll().stream().map(flyerSectionService::convertToResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlyerSectionResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(flyerSectionService.convertToResponse(flyerSectionService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlyerSectionResponse> update(@PathVariable Long id, @RequestBody FlyerSectionRequest flyerSectionRequest) {
        return ResponseEntity.ok(flyerSectionService.convertToResponse(flyerOrchestrator.updateFlyerSection(id, flyerSectionRequest)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        flyerSectionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
