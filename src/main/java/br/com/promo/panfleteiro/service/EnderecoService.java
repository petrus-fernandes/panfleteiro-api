package br.com.promo.panfleteiro.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.promo.panfleteiro.entity.Endereco;
import br.com.promo.panfleteiro.repository.EnderecoRepository;

@Service
public class EnderecoService {

    private EnderecoRepository enderecoRepository;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }
    
    public List<Endereco> create(Endereco endereco) {
        enderecoRepository.save(endereco);
        return list();
    }

    public List<Endereco> list() {
        Sort.by("nome").ascending();
        return enderecoRepository.findAll();
    }
    
    public List<Endereco> update(Endereco endereco) {
        enderecoRepository.save(endereco);
        return list();
    }
    
    public List<Endereco> delete(Long id) {
        enderecoRepository.deleteById(id);
        return list();
    }
}
