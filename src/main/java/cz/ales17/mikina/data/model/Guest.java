package cz.ales17.mikina.data.model;

import cz.geek.ubyport.StatniPrislusnost;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "guest")
public class Guest extends AbstractEntity {
    @Size(max = 50)
    @Column(length = 50)
    @NotEmpty
    private String firstName = "";

    @Size(max = 50)
    @Column(length = 50)
    @NotEmpty
    private String lastName = "";

    private LocalDate birthDate;

    @Nullable
    private LocalDate dateArrived;

    @Nullable
    private LocalDate dateLeft;

    @Size(max = 150)
    @Column(length = 150)
    private String address;

    @Enumerated(EnumType.ORDINAL)
    private StatniPrislusnost nationality;

    @Size(max = 50)
    @Column(length = 50)
    private String idNumber;

    @ManyToOne
    private Company company;

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }


}
