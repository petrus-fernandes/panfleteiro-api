package br.com.promo.panfleteiro.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.promo.panfleteiro.entity.Endereco;
import br.com.promo.panfleteiro.service.EnderecoService;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @PostMapping
    List<Endereco> create(Endereco endereco) {
        return enderecoService.create(endereco);
    }

    @GetMapping
    List<Endereco> list() {
        return enderecoService.list();
    }

    @PutMapping
    List<Endereco> update(Endereco endereco) {
        return enderecoService.update(endereco);
    }
    
    @DeleteMapping("{id}")
    List<Endereco> delete(@PathVariable Long id) {
        return enderecoService.delete(id);
    }
}
