package com.epam.epamgymdemo.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class TraineeTrainingListDto {

    private String trainingName;

    private LocalDate trainingDate;

    private String typeName;

    private Number trainingDuration;

    private String trainerName;
}
