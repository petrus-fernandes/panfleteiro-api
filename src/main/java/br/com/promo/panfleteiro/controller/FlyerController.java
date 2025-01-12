package br.com.promo.panfleteiro.controller;

import br.com.promo.panfleteiro.entity.Flyer;
import br.com.promo.panfleteiro.orchestrator.FlyerOrchestrator;
import br.com.promo.panfleteiro.request.FlyerRequest;
import br.com.promo.panfleteiro.response.FlyerResponse;
import br.com.promo.panfleteiro.service.FlyerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/panfletos")
public class FlyerController {

    private final FlyerOrchestrator flyerOrchestrator;

    private final FlyerService flyerService;

    public FlyerController(FlyerOrchestrator flyerOrchestrator, FlyerService flyerService) {
        this.flyerOrchestrator = flyerOrchestrator;
        this.flyerService = flyerService;
    }


    @PostMapping
    public ResponseEntity<FlyerResponse> create(@RequestBody FlyerRequest flyerRequest) {
        Flyer flyer = flyerOrchestrator.createFlyer(flyerRequest);
        return ResponseEntity.ok(flyerService.convertToResponse(flyer));
    }

    @GetMapping
    public ResponseEntity<List<FlyerResponse>> findAll() {
        return ResponseEntity.ok(flyerService.findAll().stream().map(flyerService::convertToResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlyerResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(flyerService.convertToResponse(flyerService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlyerResponse> update(@PathVariable Long id, @RequestBody FlyerRequest flyerRequest) {
        return ResponseEntity.ok(flyerService.convertToResponse(flyerOrchestrator.updateFlyer(id, flyerRequest)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        flyerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
