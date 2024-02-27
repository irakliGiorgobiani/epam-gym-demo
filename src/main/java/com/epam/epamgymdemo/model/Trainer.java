package com.epam.epamgymdemo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "trainer")
public class Trainer {
    @Id
    private Long trainerId;
    @ManyToOne
    @JoinColumn(name = "specialization", referencedColumnName = "typeId")
    private TrainingType trainingType;
    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
    @OneToMany(mappedBy = "trainer")
    private final List<Training> trainings = new ArrayList<>();

    public Trainer(Long trainerId, TrainingType trainingType, User user) {
        this.trainerId = trainerId;
        this.trainingType = trainingType;
        this.user = user;
    }
}
