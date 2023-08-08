package com.example.application.data.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Formula;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "country")
public class Country extends AbstractEntity {
    // source of country list https://www.czso.cz/csu/czso/ciselnik_zemi_-czem-
    @NotNull
    private String countryName;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setGuestCount(int guestCount) {
        this.guestCount = guestCount;
    }

    @NotNull
    private String countryCode;
    @OneToMany(mappedBy = "country")
    @Nullable
    private List<Guest> guests = new LinkedList<>();

    @Formula("(select count(c.id) from guest c where c.country_id = id)")
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

    public void setGuests(List<Guest> guests) {
        this.guests = guests;
    }


    public int getGuestCount() {
        return guestCount;
    }

    @Override
    public String toString() {
        return countryName + "\n";
    }
}
