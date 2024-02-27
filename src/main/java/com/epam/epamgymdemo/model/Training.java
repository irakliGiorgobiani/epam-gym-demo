package com.epam.epamgymdemo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "training")
public class Training {
    @Id
    @Column(name = "trainingId")
    private Long trainingId;
    @Column(name = "trainingName")
    private String trainingName;
    @Column(name = "trainingDate")
    private LocalDate trainingDate;
    @Column(name = "trainingDuration")
    private Number trainingDuration;

    @ManyToOne
    @JoinColumn(name = "traineeId", referencedColumnName = "traineeId")
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "trainerId", referencedColumnName = "trainerId")
    private Trainer trainer;

    @ManyToOne
    @JoinColumn(name = "typeId", referencedColumnName = "typeId")
    private TrainingType trainingType;
}
