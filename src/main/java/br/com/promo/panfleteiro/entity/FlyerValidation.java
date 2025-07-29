package br.com.promo.panfleteiro.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "flyer_validation")
@Getter
@Setter
public class FlyerValidation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String response;

    private String reservedBy;

    private LocalDateTime reserveDate;

    private LocalDateTime creationDate = LocalDateTime.now();

    public FlyerValidation(byte[] image, String response) {
        this.image = image;
        this.response = response;
    }

    public FlyerValidation() {}

}
