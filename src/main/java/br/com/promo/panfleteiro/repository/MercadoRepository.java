package br.com.promo.panfleteiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.promo.panfleteiro.entity.Mercado;

public interface MercadoRepository extends JpaRepository<Mercado, Long> {
    
}
