package br.com.promo.panfleteiro.v1.market;


import br.com.promo.panfleteiro.v1.location.LocationResponse;
import br.com.promo.panfleteiro.v1.ad.AdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MarketResponse {

    private Long id;

    private String name;

    private LocationResponse location;

    private List<AdResponse> ads = new ArrayList<>();

    private String externalCode;

    private List<Long> marketsId;

    private boolean headQuarters;

    private Double distance;

    public MarketResponse(Long id, String name, LocationResponse location, String externalCode, List<Long> marketsId, boolean headQuarters) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.externalCode = externalCode;
        this.marketsId = marketsId;
        this.headQuarters = headQuarters;
    }
}
