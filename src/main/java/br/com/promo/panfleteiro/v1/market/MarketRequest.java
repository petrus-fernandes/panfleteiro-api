package br.com.promo.panfleteiro.v1.market;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MarketRequest {

    private String name;

    private String address;

    private String externalCode;

    private List<Long> marketsId;

    private boolean headQuarters;

}
