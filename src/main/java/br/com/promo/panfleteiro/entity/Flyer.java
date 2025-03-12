package br.com.promo.panfleteiro.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "flyer")
public class Flyer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "flyer", cascade = CascadeType.ALL)
    private List<FlyerSection> flyerSections = new ArrayList<>();

    private Date expirationDate;

    private Date initialDate;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Market> markets = new ArrayList<>();

    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<FlyerSection> getFlyerSections() {
        return flyerSections;
    }

    public void setFlyerSections(List<FlyerSection> flyerSections) {
        this.flyerSections = flyerSections;
    }

    public List<Market> getMarkets() {
        return markets;
    }

    public void setMarkets(List<Market> markets) {
        this.markets = markets;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void addFlyerSection(FlyerSection flyerSection) {
        flyerSection.setFlyer(this);
        flyerSections.add(flyerSection);
    }

    public void removeFlyerSection(FlyerSection flyerSection) {
        flyerSections.remove(flyerSection);
    }

    public void addMarket(Market market) {
        market.addFlyer(this);
        markets.add(market);
    }

    public void removeMarket(Market market) {
        market.removeFlyer(this);
        markets.remove(market);
    }
}
