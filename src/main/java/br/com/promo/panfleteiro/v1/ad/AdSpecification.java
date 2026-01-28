package br.com.promo.panfleteiro.v1.ad;

import br.com.promo.panfleteiro.v1.location.Location;
import br.com.promo.panfleteiro.v1.market.Market;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public final class AdSpecification {

    public static Specification<Ad> productNameLike(String productName) {
        return (root, query, cb) -> {
            if (productName == null || productName.isBlank()) {
                return null;
            }
            return cb.like(
                    cb.lower(root.get("productName")),
                    "%" + productName.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Ad> isActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) {
                return null;
            }
            return cb.equal(root.get("active"), active);
        };
    }

    public static Specification<Ad> notExpired() {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(
                        root.get("expirationDate"),
                        LocalDate.now()
                );
    }

    public static Specification<Ad> withinBoundingBox(Double minLat, Double maxLat, Double minLon, Double maxLon) {
        return (root, query, cb) -> {

            if (minLat == null || maxLat == null || minLon == null || maxLon == null) {
                return null;
            }

            Join<Ad, Market> marketJoin = root.join("markets", JoinType.INNER);

            Join<Market, Location> locationJoin = marketJoin.join("location", JoinType.INNER);

            if (query != null) {
                query.distinct(true);
            }

            return cb.and(
                    cb.between(locationJoin.get("latitude"), minLat, maxLat),
                    cb.between(locationJoin.get("longitude"), minLon, maxLon)
            );
        };
    }
}

