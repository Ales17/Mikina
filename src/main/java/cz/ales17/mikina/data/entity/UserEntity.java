package cz.ales17.mikina.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.ales17.mikina.data.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.StringJoiner;

@Getter
@Setter
@Entity
@Table(name = "user")
public class UserEntity extends AbstractEntity {

    private String username;

    private String name;

    @Email
    private String emailAddress;

    @JsonIgnore
    private String hashedPassword;

    @Enumerated(EnumType.STRING)

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    // set name to user_roles, default will be user_entity_roles
    private Set<Role> roles;

    @Lob
    @Column(length = 1000000)
    private byte[] profilePicture;

    @ManyToOne
    private Company company;

    public String getRolesAsString() {
        StringJoiner displayRoles = new StringJoiner(", ");

        for (Role r : roles) {
            displayRoles.add(r.toString());
        }

        return displayRoles.toString();

    }
}
