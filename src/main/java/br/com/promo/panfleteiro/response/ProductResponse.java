package br.com.promo.panfleteiro.response;

import java.util.ArrayList;
import java.util.List;

public class ProductResponse {

    private Long id;
    private String name;
    private Integer productCategory;
    private List<AdResponse> ads = new ArrayList<>();

    public ProductResponse(Long id, String name, Integer productCategory) {
        this.id = id;
        this.name = name;
        this.productCategory = productCategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<AdResponse> getAds() {
        return ads;
    }

    public void setAds(List<AdResponse> ads) {
        this.ads = ads;
    }
}

