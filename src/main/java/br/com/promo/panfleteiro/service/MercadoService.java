package br.com.promo.panfleteiro.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.promo.panfleteiro.entity.Mercado;
import br.com.promo.panfleteiro.repository.MercadoRepository;

@Service
public class MercadoService {

    private MercadoRepository mercadoRepository;

    public MercadoService(MercadoRepository mercadoRepository) {
        this.mercadoRepository = mercadoRepository;
    }
    
    public List<Mercado> create(Mercado mercado) {
        mercadoRepository.save(mercado);
        return list();
    }

    public List<Mercado> list() {
        Sort.by("nome").ascending();
        return mercadoRepository.findAll();
    }
    
    public List<Mercado> update(Mercado mercado) {
        mercadoRepository.save(mercado);
        return list();
    }
    
    public List<Mercado> delete(Long id) {
        mercadoRepository.deleteById(id);
        return list();
    }
}
