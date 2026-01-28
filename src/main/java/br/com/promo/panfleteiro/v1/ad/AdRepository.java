package br.com.promo.panfleteiro.v1.ad;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Long>, JpaSpecificationExecutor<Ad> {


    @Query("SELECT a FROM Ad a WHERE UPPER(a.productName) LIKE UPPER(CONCAT('%', :productName, '%'))")
    Page<Ad> findAdsByProductName(@Param("productName") String productName, Pageable pageable);

    @Query("SELECT DISTINCT a FROM Ad a " +
            "JOIN a.markets m " +
            "JOIN m.location l " +
            "WHERE l.latitude BETWEEN :minLat AND :maxLat " +
            "AND l.longitude BETWEEN :minLon AND :maxLon " +
            "AND UPPER(a.productName) LIKE UPPER(CONCAT('%', :productName, '%')) " +
            "AND (6371 * acos(cos(radians(:baseLat)) * cos(radians(l.latitude)) " +
            "* cos(radians(l.longitude) - radians(:baseLon)) " +
            "+ sin(radians(:baseLat)) * sin(radians(l.latitude)))) <= :rangeInKm " +
            "ORDER BY a.active DESC, " +
            "a.creationDate DESC, " +
            "a.productName ASC")
    Page<Ad> findAdsByProductNameAndDistanceWithBoundingBox(@Param("minLat") double minLat,
                                                                  @Param("maxLat") double maxLat,
                                                                  @Param("minLon") double minLon,
                                                                  @Param("maxLon") double maxLon,
                                                                  @Param("baseLat") double baseLat,
                                                                  @Param("baseLon") double baseLon,
                                                                  @Param("rangeInKm") double rangeInKm,
                                                                  @Param("productName") String productName,
                                                                  Pageable pageable);

    List<Ad> findByActive(boolean active);
}