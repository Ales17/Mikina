package cz.ales17.mikina.data.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Company extends AbstractEntity {
    private String name;
    private String street;
    // Cislo domovni - house number
    private String houseNumber;
    // Cislo evidencni - registration number
    private String registrationNumber;
    private String zipCode;
    private String district;
    private String municipality;
    private String municipalityQuarter;
    private String ubyportId;
    private String ubyportAbbr;
    private String ubyportContact;
}
