package br.com.promo.panfleteiro.v1.market;

import java.io.Serializable;
import java.util.List;

import br.com.promo.panfleteiro.v1.location.Location;
import br.com.promo.panfleteiro.v1.ad.Ad;
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
    @JoinTable(name = "market_chain", joinColumns = @JoinColumn(name = "parent_market_id"), inverseJoinColumns = @JoinColumn(name = "child_market_id"))
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
