package br.com.promo.panfleteiro.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "market")
public class Market implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", foreignKey = @ForeignKey(name = "fk_location_market", value = ConstraintMode.CONSTRAINT))
    private Location location;

    @ManyToMany(mappedBy = "markets")
    private List<Ad> ads;

    public Market(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public Market(){
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Ad> getAds() {
        return ads;
    }

    public void setAds(List<Ad> ads) {
        this.ads = ads;
    }

    public void addAd(Ad ad) {
        this.ads.add(ad);
    }

    public void removeAd(Ad ad) {
        this.ads.remove(ad);
    }
}
