package cz.ales17.mikina.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.ales17.mikina.data.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
public class User extends AbstractEntity {

    private String username;

    private String name;

    @Email
    private String emailAddress;

    @JsonIgnore
    private String hashedPassword;

    @Enumerated(EnumType.STRING)

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Lob
    @Column(length = 1000000)
    private byte[] profilePicture;
}
