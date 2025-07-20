package br.com.promo.panfleteiro.request;


import java.util.List;

public class MarketRequest {

    private String name;

    private String address;

    private String externalCode;

    private List<Long> marketsId;

    private boolean headQuarters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
