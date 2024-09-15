package br.com.promo.panfleteiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.promo.panfleteiro.entity.Ad;

public interface AdRepository extends JpaRepository<Ad, Long> {

    
} 
