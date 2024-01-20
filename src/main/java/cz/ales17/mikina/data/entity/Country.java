package cz.ales17.mikina.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
@Table(name = "country")
public class Country extends AbstractEntity {
    // source of country list https://www.czso.cz/csu/czso/ciselnik_zemi_-czem-
    @NotNull
    private String countryName;

    @NotNull
    private String countryCode;

    @Override
    public String toString() {
        return countryName + "\n";
    }
}
