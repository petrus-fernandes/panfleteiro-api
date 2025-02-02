package br.com.promo.panfleteiro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.promo.panfleteiro.entity.Ad;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdRepository extends JpaRepository<Ad, Long> {


    @Query("SELECT a FROM Ad a WHERE UPPER(a.product.name) LIKE UPPER(CONCAT('%', :productName, '%'))")
    Page<Ad> findAdsByProductName(@Param("productName") String productName, Pageable pageable);
}
