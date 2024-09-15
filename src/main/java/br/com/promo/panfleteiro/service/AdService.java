package br.com.promo.panfleteiro.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.promo.panfleteiro.entity.Ad;
import br.com.promo.panfleteiro.repository.AdRepository;

@Service
public class AdService {

    private AdRepository adRepository;

    public AdService(AdRepository adRepository) {
        this.adRepository = adRepository;
    }
    
    public List<Ad> create(Ad ad) {
        adRepository.save(ad);
        return list();
    }

    public List<Ad> list() {
        Sort.by("name").ascending();
        return adRepository.findAll();
    }
    
    public List<Ad> update(Ad ad) {
        adRepository.save(ad);
        return list();
    }
    
    public List<Ad> delete(Long id) {
        adRepository.deleteById(id);
        return list();
    }
}
