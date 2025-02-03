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

    @Query("SELECT a FROM Ad a " +
            "JOIN a.flyerSection s " +
            "JOIN s.markets m " +
            "JOIN m.location l " +
            "WHERE l.latitude BETWEEN :minLat AND :maxLat " +
            "AND l.longitude BETWEEN :minLon AND :maxLon " +
            "AND (6371 * acos(cos(radians(:baseLat)) * cos(radians(l.latitude)) " +
            "* cos(radians(l.longitude) - radians(:baseLon)) " +
            "+ sin(radians(:baseLat)) * sin(radians(l.latitude)))) <= :rangeInKm " +
            "ORDER BY (6371 * acos(cos(radians(:baseLat)) * cos(radians(l.latitude)) " +
            "* cos(radians(l.longitude) - radians(:baseLon)) " +
            "+ sin(radians(:baseLat)) * sin(radians(l.latitude)))) ASC")
    Page<Ad> findAdsByDistanceWithBoundingBox(@Param("minLat") double minLat,
                                              @Param("maxLat") double maxLat,
                                              @Param("minLon") double minLon,
                                              @Param("maxLon") double maxLon,
                                              @Param("baseLat") double baseLat,
                                              @Param("baseLon") double baseLon,
                                              @Param("rangeInKm") double rangeInKm,
                                              Pageable pageable);
}
