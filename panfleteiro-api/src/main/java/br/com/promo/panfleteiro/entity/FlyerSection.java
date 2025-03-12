package br.com.promo.panfleteiro.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "flyer_section")
public class FlyerSection implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "flyer_id")
    private Flyer flyer;

    @OneToMany(mappedBy = "flyerSection", cascade = CascadeType.ALL)
    private List<Ad> ads = new ArrayList<>();

    private Date expirationDate;

    private Date initialDate;

    @ManyToMany
    private List<Market> markets;

    private Boolean active;

    public FlyerSection() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Flyer getFlyer() {
        return flyer;
    }

    public void setFlyer(Flyer flyer) {
        this.flyer = flyer;
    }

    public List<Ad> getAds() {
        return ads;
    }

    public void setAds(List<Ad> ads) {
        this.ads = ads;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public List<Market> getMarkets() {
        return markets;
    }

    public void setMarkets(List<Market> markets) {
        this.markets = markets;
    }

    public void addAd(Ad ad) {
        ad.setFlyerSection(this);
        this.ads.add(ad);
    }

    public void removeAd(Ad ad) {
        ad.setFlyerSection(null);
        this.ads.remove(ad);
    }

    public void addMarket(Market market) {
        this.markets.add(market);
    }

    public void removeMarket(Market market) {
        this.markets.remove(market);
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void addFlyer(Flyer flyer) {
        flyer.addFlyerSection(this);
        this.flyer = flyer;
    }

    public void removeFlyer(Flyer flyer) {
        flyer.removeFlyerSection(this);
        this.flyer = null;
    }
}
