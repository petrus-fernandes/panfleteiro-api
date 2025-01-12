package br.com.promo.panfleteiro.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.promo.panfleteiro.request.MarketRequest;
import br.com.promo.panfleteiro.response.MarketResponse;
import br.com.promo.panfleteiro.service.MarketService;

@RestController
@RequestMapping("/v1/mercados")
public class MarketController {

    private final MarketService marketService;

    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping
    public ResponseEntity<List<MarketResponse>> findAll() {
        return ResponseEntity.ok(marketService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarketResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(marketService.getMarketResponseById(id));
    }

    @PostMapping
    public ResponseEntity<MarketResponse> create(@RequestBody MarketRequest marketRequest) {
        return ResponseEntity.status(201).body(marketService.create(marketRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarketResponse> update(@PathVariable Long id, @RequestBody MarketRequest marketRequest) {
        return ResponseEntity.ok(marketService.update(id, marketRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        marketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nome")
    public ResponseEntity<List<MarketResponse>> findByNameContaining(@RequestParam String name) {
        List<MarketResponse> markets = marketService.findByName(name);
        return ResponseEntity.ok(markets);
    }
}
