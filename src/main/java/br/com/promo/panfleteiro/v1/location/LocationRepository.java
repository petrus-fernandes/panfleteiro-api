package br.com.promo.panfleteiro.v1.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("SELECT l FROM Location l WHERE l.address = ?1")
    Optional<Location> findByAddress(String address);

    @Query(value = """
        SELECT locations.*,
               (6371 * ACOS(
                   COS(RADIANS(:lat)) * COS(RADIANS(locations.latitude)) *
                   COS(RADIANS(locations.longitude) - RADIANS(:lng)) +
                   SIN(RADIANS(:lat)) * SIN(RADIANS(locations.latitude))
               )) AS distance
        FROM locations locations
        HAVING distance <= :radius
        ORDER BY distance
    """, nativeQuery = true)
    List<Location> findNearbyLocations(
            @Param("lat") double latitude,
            @Param("lng") double longitude,
            @Param("radius") double radius
    );
} 
