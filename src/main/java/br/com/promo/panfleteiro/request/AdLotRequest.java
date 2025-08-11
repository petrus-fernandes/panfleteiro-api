package br.com.promo.panfleteiro.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class AdLotRequest {

    private String url;
    private List<Long> marketsId;

    private String marketExternalCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate initialDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate  expirationDate;

    private List<AdRequest> ads;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Long> getMarketsId() {
        return marketsId;
    }

    public void setMarketsId(List<Long> marketsId) {
        this.marketsId = marketsId;
    }

    public String getMarketExternalCode() {
        return marketExternalCode;
    }

    public void setMarketExternalCode(String marketExternalCode) {
        this.marketExternalCode = marketExternalCode;
    }

    public LocalDate  getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(LocalDate  initialDate) {
        this.initialDate = initialDate;
    }

    public LocalDate  getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate  expirationDate) {
        this.expirationDate = expirationDate;
    }

    public List<AdRequest> getAds() {
        return ads;
    }

    public void setAds(List<AdRequest> ads) {
        this.ads = ads;
    }
}
