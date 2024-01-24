package cz.ales17.mikina.data.entity;

import cz.geek.ubyport.StatniPrislusnost;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "guest")
public class Guest extends AbstractEntity {

    @NotEmpty
    private String firstName = "";

    @NotEmpty
    private String lastName = "";

    private LocalDate birthDate;
    @Nullable
    private LocalDate dateArrived;
    @Nullable
    private LocalDate dateLeft;

    private String address;

    @Enumerated(EnumType.ORDINAL)
    private StatniPrislusnost nationality;

    private String idNumber;

    @ManyToOne
    private Company company;

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }


}
