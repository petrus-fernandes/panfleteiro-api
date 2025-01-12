package br.com.promo.panfleteiro.response;


public class MarketResponse {

    private Long id;

    private String name;

    private LocationResponse location;

    public MarketResponse(Long id, String name, LocationResponse location) {
        this.id = id;
        this.name = name;
        this.location = location;
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

}
