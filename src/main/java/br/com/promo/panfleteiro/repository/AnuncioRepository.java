package br.com.promo.panfleteiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.promo.panfleteiro.entity.Anuncio;

public interface AnuncioRepository extends JpaRepository<Anuncio, Long> {

    
} 
