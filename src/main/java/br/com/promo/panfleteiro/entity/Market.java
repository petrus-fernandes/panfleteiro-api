package br.com.promo.panfleteiro.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "market")
@Getter
@Setter
@NoArgsConstructor
public class Market implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "location_id", foreignKey = @ForeignKey(name = "fk_location_market", value = ConstraintMode.CONSTRAINT))
    private Location location;

    @ManyToMany(mappedBy = "markets")
    private List<Ad> ads;

    @Column(unique = true)
    private String externalCode;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Market> marketChain;

    private boolean headQuarters;

    public Market(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public void addAd(Ad ad) {
        this.ads.add(ad);
    }

    public void removeAd(Ad ad) {
        this.ads.remove(ad);
    }

    public boolean isMarketInRange(double latitude, double longitude, double range) {
        return this.getLocation().calculateDistanceInKm(latitude, longitude) <= range;
    }
}
