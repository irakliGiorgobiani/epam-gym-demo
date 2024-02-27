package com.epam.epamgymdemo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "trainee")
public class Trainee {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(name = "address")
    private String address;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL)
    private final List<Training> trainings = new ArrayList<>();
    @ManyToMany
    @JoinTable(
            name = "trainee_trainer",
            joinColumns = { @JoinColumn(name = "trainee_id") },
            inverseJoinColumns = { @JoinColumn(name = "trainer_id") }
    )
    private Set<Trainer> trainers = new HashSet<>();
}
