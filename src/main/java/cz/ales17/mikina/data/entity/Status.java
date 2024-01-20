package cz.ales17.mikina.data.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Status extends AbstractEntity {
    private String name;

    public Status() {
    }

}
