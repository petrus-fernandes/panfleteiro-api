package br.com.promo.panfleteiro.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.promo.panfleteiro.entity.Ad;
import br.com.promo.panfleteiro.service.AdService;

@RestController
@RequestMapping("/ads")
public class AdController {

    AdService adService;

    public AdController(AdService adService) {
        this.adService = adService;
    }

    @PostMapping
    List<Ad> create(Ad ad) {
        return adService.create(ad);
    }

    @GetMapping
    List<Ad> list() {
        return adService.list();
    }

    @PutMapping
    List<Ad> update(Ad ad) {
        return adService.update(ad);
    }
    
    @DeleteMapping("{id}")
    List<Ad> delete(@PathVariable Long id) {
        return adService.delete(id);
    }
}
