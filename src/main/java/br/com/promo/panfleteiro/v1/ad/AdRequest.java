package br.com.promo.panfleteiro.v1.ad;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AdRequest {

    private String productName;
    private String productCategory;
    private BigDecimal price;
    private String priceWithDiscount;
    private Boolean active;
    private String url;
    private List<Long> marketsId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate  initialDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate expirationDate;

}

