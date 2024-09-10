package br.com.promo.panfleteiro.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.promo.panfleteiro.entity.Mercado;
import br.com.promo.panfleteiro.service.MercadoService;

@RestController
@RequestMapping("/mercados")
public class MercadoController {

    MercadoService mercadoService;

    public MercadoController(MercadoService mercadoService) {
        this.mercadoService = mercadoService;
    }

    @PostMapping
    List<Mercado> create(Mercado mercado) {
        return mercadoService.create(mercado);
    }

    @GetMapping
    List<Mercado> list() {
        return mercadoService.list();
    }

    @PutMapping
    List<Mercado> update(Mercado mercado) {
        return mercadoService.update(mercado);
    }
    
    @DeleteMapping("{id}")
    List<Mercado> delete(@PathVariable Long id) {
        return mercadoService.delete(id);
    }
}
