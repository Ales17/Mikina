package com.example.application.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cz.geek.ubyport.StatniPrislusnost;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "guest")
public class Guest extends AbstractEntity {

    @NotEmpty
    private String firstName = "";

    @NotEmpty
    private String lastName = "";

    private LocalDate birthDate;

    private LocalDate dateArrived;

    private LocalDate dateLeft;

    private String address;
    @Enumerated(EnumType.ORDINAL)
    private StatniPrislusnost nationality;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateArrived() {
        return dateArrived;
    }

    public void setDateArrived(LocalDate dateArrived) {
        this.dateArrived = dateArrived;
    }

    public LocalDate getDateLeft() {
        return dateLeft;
    }

    @NotEmpty
    private String idNumber;


    @ManyToOne
    @JoinColumn(name = "country_id")
    @NotNull
    @JsonIgnoreProperties({"employees"})
    private Country country;

    public void setDateLeft(LocalDate dateLeft) {
        this.dateLeft = dateLeft;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public StatniPrislusnost getNationality() {
        return nationality;
    }

    public void setNationality(StatniPrislusnost nationality) {
        this.nationality = nationality;
    }


}
