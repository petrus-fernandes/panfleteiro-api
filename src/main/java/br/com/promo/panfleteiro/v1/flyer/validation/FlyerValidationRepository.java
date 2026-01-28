package br.com.promo.panfleteiro.v1.flyer.validation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlyerValidationRepository extends JpaRepository<FlyerValidation, Long> {

    @Query("SELECT r FROM FlyerValidation r WHERE (r.reservedBy IS NULL OR r.reserveDate < :expiration) ORDER BY r.creationDate ASC")
    List<FlyerValidation> findAvaiablesValidation(@Param("expiration") LocalDateTime expiration);

    Optional<FlyerValidation> findByIdAndReservedBy(Long id, String reservedBy);

    @Query("SELECT r FROM FlyerValidation r WHERE (r.reservedBy IS NULL OR r.reserveDate < :expiration) ORDER BY r.creationDate ASC")
    Optional<FlyerValidation> findFirstAvaiable(@Param("expiration") LocalDateTime expiration);
}

