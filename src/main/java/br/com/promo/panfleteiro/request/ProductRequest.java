package br.com.promo.panfleteiro.request;

import java.util.ArrayList;
import java.util.List;

public class ProductRequest {
    private String name;
    private Integer productCategory;
    private List<Long> adsId = new ArrayList<>();

    public ProductRequest(String name, Integer productCategory, List<Long> adsId) {
        this.name = name;
        this.productCategory = productCategory;
        this.adsId = adsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(Integer productCategory) {
        this.productCategory = productCategory;
    }

    public List<Long> getAdsId() {
        return adsId;
    }

    public void setAdsId(List<Long> adsId) {
        this.adsId = adsId;
    }
}
