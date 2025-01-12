package br.com.promo.panfleteiro.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AdResponse {
    
    private Long id;

    private Long productId;

    private String productName;

    private BigDecimal price;

    private Double distance;

    private Boolean active;

    private String url;

    private Long flyerSectionId;

    private List<Long> marketsId = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getMarketsId() {
        return marketsId;
    }

    public void setMarketsId(List<Long> marketsId) {
        this.marketsId = marketsId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }


    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getFlyerSectionId() {
        return flyerSectionId;
    }

    public void setFlyerSectionId(Long flyerSectionId) {
        this.flyerSectionId = flyerSectionId;
    }
}
