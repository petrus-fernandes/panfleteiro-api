package br.com.promo.panfleteiro.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.promo.panfleteiro.entity.Anuncio;
import br.com.promo.panfleteiro.repository.AnuncioRepository;

@Service
public class AnuncioService {

    private AnuncioRepository anuncioRepository;

    public AnuncioService(AnuncioRepository anuncioRepository) {
        this.anuncioRepository = anuncioRepository;
    }
    
    public List<Anuncio> create(Anuncio anuncio) {
        anuncioRepository.save(anuncio);
        return list();
    }

    public List<Anuncio> list() {
        Sort.by("nome").ascending();
        return anuncioRepository.findAll();
    }
    
    public List<Anuncio> update(Anuncio anuncio) {
        anuncioRepository.save(anuncio);
        return list();
    }
    
    public List<Anuncio> delete(Long id) {
        anuncioRepository.deleteById(id);
        return list();
    }
}
