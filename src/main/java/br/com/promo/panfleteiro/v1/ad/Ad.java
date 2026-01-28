package br.com.promo.panfleteiro.v1.ad;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import br.com.promo.panfleteiro.v1.market.Market;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ad")
public class Ad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private ProductCategory productCategory;

    private String url;

    private Boolean active;

    private BigDecimal price;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Market> markets;

    @NotNull
    private LocalDate initialDate;

    @NotNull
    private LocalDate expirationDate;

    private final LocalDateTime creationDate = LocalDateTime.now();

    public Ad(String url, Boolean active, BigDecimal price) {
        this.url = url;
        this.active = active;
        this.price = price;
    }

    public Ad() {
    }

}
