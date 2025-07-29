package br.com.promo.panfleteiro.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlyerValidationResponse {

    private Long id;
    private String imageBase64;
    private String response;

    public FlyerValidationResponse(Long id, String imageBase64, String response) {
        this.id = id;
        this.imageBase64 = imageBase64;
        this.response = response;
    }

}
