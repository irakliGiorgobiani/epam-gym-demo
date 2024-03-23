package com.epam.epamgymdemo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeTrainingDto {

    private String trainingName;

    private LocalDate trainingDate;

    private String trainingType;

    private Number trainingDuration;

    private String trainerName;
}