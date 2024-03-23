package com.epam.epamgymdemo.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDto {

    @JsonProperty(required = true)
    private String traineeUsername;

    @JsonProperty(required = true)
    private String trainerUsername;

    @JsonProperty(required = true)
    private String trainingName;

    @JsonProperty(required = true)
    private LocalDate trainingDate;

    @JsonProperty(required = true)
    private Number trainingDuration;
}