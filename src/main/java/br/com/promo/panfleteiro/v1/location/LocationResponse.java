package br.com.promo.panfleteiro.v1.location;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationResponse {

    private Long id;

    private Double latitude;

    private Double longitude;
    
    private String address;

    private Boolean active;

    public LocationResponse(Long id, Double latitude, Double longitude, String address, Boolean active) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.active = active;
    }
}
