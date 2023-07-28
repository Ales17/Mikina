package com.example.application.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDate;

@Entity
@Table(name="guest")
public class Guest extends AbstractEntity {

    @NotEmpty
    private String firstName = "";

    @NotEmpty
    private String lastName = "";

    private LocalDate birthDate;

    private LocalDate dateArrived;

    private LocalDate dateLeft;


    public LocalDate getDateArrived() {
        return dateArrived;
    }

    public void setDateArrived(LocalDate dateArrived) {
        this.dateArrived = dateArrived;
    }

    public LocalDate getDateLeft() {
        return dateLeft;
    }

    public void setDateLeft(LocalDate dateLeft) {
        this.dateLeft = dateLeft;
    }

    @ManyToOne
    @JoinColumn(name = "country_id")
    @NotNull
    @JsonIgnoreProperties({"employees"})
    private Country country;

/*
    @ManyToOne
    private Status status;*/

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

    @NotEmpty
    private String idNumber;

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

   /* public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }*/

}
