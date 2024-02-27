package com.epam.epamgymdemo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "training_type")
public class TrainingType {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "type_name", nullable = false)
    private String typeName;

    @OneToMany(mappedBy = "trainingType")
    private final List<Trainer> trainers = new ArrayList<>();

    @OneToMany(mappedBy = "trainingType")
    private final List<Training> trainings = new ArrayList<>();
}
