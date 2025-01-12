package br.com.promo.panfleteiro.response;

import java.util.Date;
import java.util.List;

public class FlyerSectionResponse {

    private Long id;
    private Long flyerId;
    private Date expirationDate;
    private Date initialDate;

    private Boolean active;
    private List<Long> adsId;

    private List<Long> marketsId;

    public FlyerSectionResponse() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFlyerId() {
        return flyerId;
    }

    public void setFlyerId(Long flyerId) {
        this.flyerId = flyerId;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public List<Long> getAdsId() {
        return adsId;
    }

    public void setAdsId(List<Long> adsId) {
        this.adsId = adsId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<Long> getMarketsId() {
        return marketsId;
    }

    public void setMarketsId(List<Long> marketsId) {
        this.marketsId = marketsId;
    }
}
