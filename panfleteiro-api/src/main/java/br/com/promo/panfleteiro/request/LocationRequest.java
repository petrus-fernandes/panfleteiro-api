package br.com.promo.panfleteiro.request;

public class LocationRequest {
    
    private String address;

    private Boolean active;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
