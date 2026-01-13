package br.com.promo.panfleteiro.ad;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdSearchRequest {
    private String productName;
    private Boolean active = true;
    private Double latitude;
    private Double longitude;
    private Long rangeInKm = 10L;
    private String cep;

    public boolean hasCep(){
        return cep != null && !cep.isBlank();
    }

    public boolean hasLatitudeAndLongitude() {
        return latitude != null && longitude != null;
    }

    public boolean hasLatitudeOrLongitude() {
        return latitude != null || longitude != null;
    }

}

