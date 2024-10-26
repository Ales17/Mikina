package cz.ales17.mikina.data.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

// Column lengths are set to be compatible with UNL (Ubyport)
@Getter
@Setter
@Entity
public class Company extends AbstractEntity {
    @Column(length = 14)
    private String ubyportId;

    @Column(length = 5)
    private String ubyportAbbr;

    @Column(length = 35)
    private String name;

    @Column(length = 50)
    private String ubyportContact;

    @Column(length = 32)
    private String district;

    @Column(length = 48)
    private String municipality;

    @Column(length = 48)
    private String municipalityQuarter;

    @Column(length = 48)
    private String street;

    // Cislo domovni - house number
    @Column(length = 5)
    private String houseNumber;

    // Cislo evidencni - registration number
    @Column(length = 4)
    private String registrationNumber;

    @Column(length = 5)
    private String zipCode;

    // Ubytovaci poplatek - stay fee per day (except the first one)
    private double stayFee;
}
