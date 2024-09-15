package br.com.promo.panfleteiro.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.promo.panfleteiro.entity.Market;
import br.com.promo.panfleteiro.service.MarketService;

@RestController
@RequestMapping("/mercados")
public class MarketController {

    MarketService marketService;

    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    @PostMapping
    List<Market> create(Market market) {
        return marketService.create(market);
    }

    @GetMapping
    List<Market> list() {
        return marketService.list();
    }

    @PutMapping
    List<Market> update(Market market) {
        return marketService.update(market);
    }
    
    @DeleteMapping("{id}")
    List<Market> delete(@PathVariable Long id) {
        return marketService.delete(id);
    }
}
