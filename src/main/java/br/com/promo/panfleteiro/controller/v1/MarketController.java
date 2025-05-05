package br.com.promo.panfleteiro.controller.v1;

import java.util.List;

import br.com.promo.panfleteiro.helper.AdMarketHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.promo.panfleteiro.request.MarketRequest;
import br.com.promo.panfleteiro.response.MarketResponse;
import br.com.promo.panfleteiro.service.MarketService;

@RestController
@RequestMapping("/v1/mercados")
public class MarketController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    private AdMarketHelper adMarketHelper;

    @Autowired
    private MarketService marketService;

    @GetMapping
    public ResponseEntity<List<MarketResponse>> findAll() {
        logger.info("Listing all Markets");
        return ResponseEntity.ok(marketService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarketResponse> findById(@PathVariable Long id) {
        logger.info("Looking for Market with ID: {}", id);
        return ResponseEntity.ok(adMarketHelper.getMarketResponseById(id));
    }

    @PostMapping
    public ResponseEntity<MarketResponse> create(@RequestBody MarketRequest marketRequest) {
        logger.info("Creating Market: {}", marketRequest);
        MarketResponse marketResponse = marketService.create(marketRequest);
        logger.info("Created Market: {}", marketResponse.getId());
        return ResponseEntity.status(201).body(marketResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarketResponse> update(@PathVariable Long id, @RequestBody MarketRequest marketRequest) {
        logger.info("Updating Market with ID: {}", id);
        return ResponseEntity.ok(marketService.update(id, marketRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Deleting Market with ID: {}", id);
        marketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nome")
    public ResponseEntity<List<MarketResponse>> findByNameContaining(@RequestParam String name) {
        logger.info("Searching for Markets by name: {}", name);
        List<MarketResponse> markets = marketService.findByName(name);
        logger.info("Found {} Markets by name.", markets.size());
        return ResponseEntity.ok(markets);
    }
}
