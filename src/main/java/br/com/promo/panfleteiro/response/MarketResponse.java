package br.com.promo.panfleteiro.response;


import java.util.ArrayList;
import java.util.List;

public class MarketResponse {

    private Long id;

    private String name;

    private LocationResponse location;

    private List<AdResponse> ads = new ArrayList<>();

    private String externalCode;

    private List<Long> marketsId;

    private boolean headQuarters;

    public MarketResponse(Long id, String name, LocationResponse location, String externalCode, List<Long> marketsId, boolean headQuarters) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.externalCode = externalCode;
        this.marketsId = marketsId;
        this.headQuarters = headQuarters;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationResponse getLocation() {
        return location;
    }

    public void setLocation(LocationResponse location) {
        this.location = location;
    }

    public List<AdResponse> getAds() {
        return ads;
    }

    public void setAds(List<AdResponse> ads) {
        this.ads = ads;
    }

    public String getExternalCode() {
        return externalCode;
    }

    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    public List<Long> getMarketsId() {
        return marketsId;
    }

    public void setMarketsId(List<Long> marketsId) {
        this.marketsId = marketsId;
    }

    public boolean isHeadQuarters() {
        return headQuarters;
    }

    public void setHeadQuarters(boolean headQuarters) {
        this.headQuarters = headQuarters;
    }
}
