package com.epam.epamgymdemo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "trainingType")
public class TrainingType {
    @Id
    @Column(name = "typeId")
    private Long typeId;
    @Column(name = "typeName")
    private String typeName;

    @OneToMany(mappedBy = "trainingType")
    private final List<Trainer> trainers = new ArrayList<>();

    @OneToMany(mappedBy = "trainingType")
    private final List<Training> trainings = new ArrayList<>();

    public TrainingType(Long typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }
}
