package br.com.promo.panfleteiro.v1.location;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationRequest {
    
    private String address;

    private Boolean active;

}
