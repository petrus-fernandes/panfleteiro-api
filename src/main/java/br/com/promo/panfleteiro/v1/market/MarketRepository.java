package br.com.promo.panfleteiro.v1.market;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MarketRepository extends JpaRepository<Market, Long> {

    List<Market> findByNameContainingIgnoreCase(String name);

    Market findByExternalCode(String marketExternalCode);

    boolean existsByExternalCode(String externalCode);

    @Query("SELECT m FROM Market m JOIN m.marketChain mc WHERE mc.id = :marketId")
    Optional<Market> findMarketContainingMarketId(@Param("marketId") Long marketId);
}
