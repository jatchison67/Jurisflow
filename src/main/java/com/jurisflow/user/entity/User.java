package com.jurisflow.user.entity;

import com.jurisflow.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_email",
                        columnNames = "email"
                )
        }
)
public class User extends BaseEntity {


    @Column(nullable = false)
    private String email;


    @Column(nullable = false)
    private String firstName;


    @Column(nullable = false)
    private String lastName;


    @Column(nullable = false)
    private boolean active = true;


    protected User() {
    }


    public User(
            String email,
            String firstName,
            String lastName
    ) {

        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;

    }


    public String getEmail() {
        return email;
    }


    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public boolean isActive() {
        return active;
    }


    public void deactivate() {

        this.active = false;

    }

}