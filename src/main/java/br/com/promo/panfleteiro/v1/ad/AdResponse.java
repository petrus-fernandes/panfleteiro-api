package br.com.promo.panfleteiro.v1.ad;

import br.com.promo.panfleteiro.v1.market.MarketResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class AdResponse {
    
    private Long id;

    private String productName;

    private String productCategory;

    private BigDecimal price;

    private Boolean active;

    private List<MarketResponse> markets;

    private String url;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate initialDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate expirationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy:HH:mm:ss")
    private LocalDateTime creationDate;

    private Double nearestMarketDistance;

    public void orderMarketsByDistanceInRange(Long rangeInKm) {
        if (this.markets == null) {
            this.nearestMarketDistance = null;
            return;
        }

        this.markets.removeIf(Objects::isNull);

        this.markets.sort(Comparator.comparingDouble(MarketResponse::getDistance));
        this.markets.removeIf(market -> market.getDistance() > rangeInKm);
        this.nearestMarketDistance = this.markets.stream().findFirst().map(MarketResponse::getDistance).orElse(null);
    }
}
