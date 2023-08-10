package cz.ales17.mikina.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "country")
public class Country extends AbstractEntity {
    // source of country list https://www.czso.cz/csu/czso/ciselnik_zemi_-czem-
    @NotNull
    private String countryName;
    @NotNull
    private String countryCode;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String name) {
        this.countryName = name;
    }

    @Override
    public String toString() {
        return countryName + "\n";
    }
}
