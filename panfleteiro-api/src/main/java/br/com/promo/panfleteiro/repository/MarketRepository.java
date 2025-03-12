package br.com.promo.panfleteiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.promo.panfleteiro.entity.Market;

import java.util.List;

public interface MarketRepository extends JpaRepository<Market, Long> {

    List<Market> findByNameContainingIgnoreCase(String name);
}
