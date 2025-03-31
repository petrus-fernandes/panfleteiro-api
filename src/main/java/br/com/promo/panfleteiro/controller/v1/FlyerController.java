package br.com.promo.panfleteiro.controller.v1;

import br.com.promo.panfleteiro.entity.Flyer;
import br.com.promo.panfleteiro.orchestrator.FlyerOrchestrator;
import br.com.promo.panfleteiro.request.FlyerRequest;
import br.com.promo.panfleteiro.response.FlyerResponse;
import br.com.promo.panfleteiro.service.FlyerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/panfletos")
public class FlyerController {

    private final FlyerOrchestrator flyerOrchestrator;

    private final FlyerService flyerService;

    private static final Logger logger = LoggerFactory.getLogger(FlyerController.class);
    public FlyerController(FlyerOrchestrator flyerOrchestrator, FlyerService flyerService) {
        this.flyerOrchestrator = flyerOrchestrator;
        this.flyerService = flyerService;
    }


    @PostMapping
    public ResponseEntity<FlyerResponse> create(@RequestBody FlyerRequest flyerRequest) {
        logger.info("Creating FlyerRequest: {}", flyerRequest);
        Flyer flyer = flyerOrchestrator.createFlyer(flyerRequest);
        logger.info("Created FlyerResponse: {}", flyer.getId());
        return ResponseEntity.ok(flyerService.convertToResponse(flyer));
    }

    @GetMapping
    public ResponseEntity<List<FlyerResponse>> findAll() {
        logger.info("Listing all Flyers");
        return ResponseEntity.ok(flyerService.findAll().stream().map(flyerService::convertToResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlyerResponse> findById(@PathVariable Long id) {
        logger.info("Looking for FlyerResponse with ID: {}", id);
        return ResponseEntity.ok(flyerService.convertToResponse(flyerService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlyerResponse> update(@PathVariable Long id, @RequestBody FlyerRequest flyerRequest) {
        logger.info("Updating FlyerRequest with ID: {}", id);
        return ResponseEntity.ok(flyerService.convertToResponse(flyerOrchestrator.updateFlyer(id, flyerRequest)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Deleting FlyerResponse with ID: {}", id);
        flyerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
