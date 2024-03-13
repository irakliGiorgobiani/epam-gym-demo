package com.epam.epamgymdemo.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TrainingDto {

    private String trainingName;

    private LocalDate trainingDate;

    private Number trainingDuration;

    private TraineeDto trainee;

    private TrainerDto trainer;

    private TrainingTypeDto trainingType;
}
