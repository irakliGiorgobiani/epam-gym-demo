package com.epam.epamgymdemo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "training")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Training {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "training_name", nullable = false)
    private String trainingName;
    @Column(name = "training_date", nullable = false)
    private LocalDate trainingDate;
    @Column(name = "training_duration", nullable = false)
    private Number trainingDuration;

    @ManyToOne
    @JoinColumn(name = "trainee_id", referencedColumnName = "id", nullable = false)
    @JsonManagedReference
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "trainer_id", referencedColumnName = "id", nullable = false)
    @JsonManagedReference
    private Trainer trainer;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id", nullable = false)
    @JsonManagedReference
    private TrainingType trainingType;
}