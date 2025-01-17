package br.com.promo.panfleteiro.entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "ad")
public class Ad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    private String url;

    private Boolean active;

    private BigDecimal price;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "flyer_section_id")
    private FlyerSection flyerSection;

    public Ad(Product product, String url, Boolean active, BigDecimal price, FlyerSection flyerSection) {
        this.product = product;
        this.url = url;
        this.active = active;
        this.price = price;
        this.flyerSection = flyerSection;
    }

    public Ad() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public FlyerSection getFlyerSection() {
        return flyerSection;
    }

    public void setFlyerSection(FlyerSection flyerSection) {
        this.flyerSection = flyerSection;
    }

    public void addProduct(Product product) {
        product.addAd(this);
        this.product = product;
    }

    public void removeProduct() {
        this.product.removeAd(this);
        this.product = null;
    }

    public void removeFlyerSection() {
        this.flyerSection.removeAd(this);
        this.flyerSection = null;
    }
}
