package br.com.promo.panfleteiro.repository;

import br.com.promo.panfleteiro.entity.Flyer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlyerRepository extends JpaRepository<Flyer, Long> {
}
