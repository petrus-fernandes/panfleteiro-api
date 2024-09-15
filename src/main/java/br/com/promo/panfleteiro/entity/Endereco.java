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
@Table(name = "endereco")
public class Endereco implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latitude;

    private Double longitude;
    
    private String logradouro;

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
    public String getLogradouro() {
        return logradouro;
    }
    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public Double calcularDistanciaEmKm(Endereco endereco) {
        return 6371.0 * acos(sin(endereco.latitude)*sin(this.latitude)+cos(endereco.latitude)*cos(this.latitude)*cos(endereco.longitude-this.longitude));
    }
}
