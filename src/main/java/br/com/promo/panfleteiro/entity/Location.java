package br.com.promo.panfleteiro.entity;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.acos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "location")
public class Location implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latitude;

    private Double longitude;

    @Column(unique = true, nullable = false)
    private String address;

    private Boolean active;

    public Location(Long id, Double latitude, Double longitude, String address, Boolean active) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.active = active;
    }

    public Location() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Double calculateDistanceInKm(Location location) {
        return 6371.0 * acos(sin(location.latitude) * sin(this.latitude) + cos(location.latitude) * cos(this.latitude) * cos(location.longitude - this.longitude));
    }
}
