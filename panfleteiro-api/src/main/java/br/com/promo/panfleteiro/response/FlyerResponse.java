package br.com.promo.panfleteiro.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class FlyerResponse {

    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date initialDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date expirationDate;

    private Boolean active;

    private List<Long> flyerSectionsId;

    private List<Long> marketsId;

    public FlyerResponse(Long id, Date expirationDate, Date initialDate, List<Long> marketsId, Boolean active, List<Long> flyerSectionsId) {
        this.id = id;
        this.expirationDate = expirationDate;
        this.initialDate = initialDate;
        this.marketsId = marketsId;
        this.active = active;
        this.flyerSectionsId = flyerSectionsId;
    }

    public Long getId() {
        return id;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public List<Long> getMarketsId() {
        return marketsId;
    }

    public Boolean getActive() {
        return active;
    }

    public List<Long> getFlyerSectionsId() {
        return flyerSectionsId;
    }

    public void setFlyerSectionsId(List<Long> flyerSectionsId) {
        this.flyerSectionsId = flyerSectionsId;
    }
}
