package br.com.promo.panfleteiro.repository;

import br.com.promo.panfleteiro.entity.Flyer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlyerRepository extends JpaRepository<Flyer, Long> {
    List<Flyer> findByActive(boolean active);
}
