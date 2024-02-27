package com.epam.epamgymdemo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "trainee")
public class Trainee {
    @Id
    @Column(name = "traineeId")
    private Long traineeId;

    @Column(name = "dateOfBirth")
    private LocalDate dateOfBirth;
    @Column(name = "address")
    private String address;
    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    @OneToMany(mappedBy = "trainee")
    private final List<Training> trainings = new ArrayList<>();

    public Trainee(Long traineeId, LocalDate dateOfBirth, String address, User user) {
        this.traineeId = traineeId;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.user = user;
    }
}
