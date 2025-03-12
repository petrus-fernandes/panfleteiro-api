package br.com.promo.panfleteiro.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class AdRequest {

    private List<Long> marketsId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    private BigDecimal price;

    private Boolean active;

    private String url;

    private Long flyerSectionId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public Long getFlyerSectionId() {
        return flyerSectionId;
    }

    public void setFlyerSectionId(Long flyerSectionIds) {
        this.flyerSectionId = flyerSectionIds;
    }
}

