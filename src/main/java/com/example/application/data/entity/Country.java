package com.example.application.data.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.Formula;

import java.util.LinkedList;
import java.util.List;

@Entity
public class Country extends AbstractEntity {

    private String countryName;

    @OneToMany(mappedBy = "country")
    @Nullable
    private List<Guest> guests = new LinkedList<>();

    @Formula("(select count(c.id) from Guest c where c.country_id = id)")
    private int guestCount;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String name) {
        this.countryName = name;
    }

    public List<Guest> getGuests() {
        return guests;
    }

    public void setGuests(List<Guest> employees) {
        this.guests = employees;
    }


    public int getGuestCount() {
        return guestCount;
    }
}
