package br.com.promo.panfleteiro.repository;

import br.com.promo.panfleteiro.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
