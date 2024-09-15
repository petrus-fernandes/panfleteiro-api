package br.com.promo.panfleteiro.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.promo.panfleteiro.entity.Market;
import br.com.promo.panfleteiro.repository.MarketRepository;

@Service
public class MarketService {

    private MarketRepository marketRepository;

    public MarketService(MarketRepository marketRepository) {
        this.marketRepository = marketRepository;
    }
    
    public List<Market> create(Market market) {
        marketRepository.save(market);
        return list();
    }

    public List<Market> list() {
        Sort.by("name").ascending();
        return marketRepository.findAll();
    }
    
    public List<Market> update(Market market) {
        marketRepository.save(market);
        return list();
    }
    
    public List<Market> delete(Long id) {
        marketRepository.deleteById(id);
        return list();
    }
}
