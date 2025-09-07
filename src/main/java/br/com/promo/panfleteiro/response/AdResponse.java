package br.com.promo.panfleteiro.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
}
