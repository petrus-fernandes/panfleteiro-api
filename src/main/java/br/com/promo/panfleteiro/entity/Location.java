package br.com.promo.panfleteiro.entity;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.acos;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "address")
public class Location implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latitude;

    private Double longitude;
    
    private String address;

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
    public void setAddress(String logradouro) {
        this.address = logradouro;
    }

    public Double calculateDistanceInKm(Location address) {
        return 6371.0 * acos(sin(address.latitude)*sin(this.latitude)+cos(address.latitude)*cos(this.latitude)*cos(address.longitude-this.longitude));
    }
}
