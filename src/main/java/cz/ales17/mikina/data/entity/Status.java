package cz.ales17.mikina.data.entity;

import jakarta.persistence.Entity;

@Entity
public class Status extends AbstractEntity {
    private String name;

    public Status() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
