package com.epam.epamgymdemo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "userId")
    private Long userId;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "userName")
    private String userName;
    @Column(name = "password")
    private String password;
    @Column(name = "isActive")
    private Boolean isActive;
    @OneToOne(mappedBy = "user")
    private Trainee trainee;
    @OneToOne(mappedBy = "user")
    private Trainer trainer;

    public User(Long userId, String firstName, String lastName, String userName, String password, Boolean isActive) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.isActive = isActive;
    }
}
