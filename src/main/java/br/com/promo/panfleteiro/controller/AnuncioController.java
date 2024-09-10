package br.com.promo.panfleteiro.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.promo.panfleteiro.entity.Anuncio;
import br.com.promo.panfleteiro.service.AnuncioService;

@RestController
@RequestMapping("/anuncios")
public class AnuncioController {

    AnuncioService anuncioService;

    public AnuncioController(AnuncioService anuncioService) {
        this.anuncioService = anuncioService;
    }

    @PostMapping
    List<Anuncio> create(Anuncio anuncio) {
        return anuncioService.create(anuncio);
    }

    @GetMapping
    List<Anuncio> list() {
        return anuncioService.list();
    }

    @PutMapping
    List<Anuncio> update(Anuncio anuncio) {
        return anuncioService.update(anuncio);
    }
    
    @DeleteMapping("{id}")
    List<Anuncio> delete(@PathVariable Long id) {
        return anuncioService.delete(id);
    }
}
