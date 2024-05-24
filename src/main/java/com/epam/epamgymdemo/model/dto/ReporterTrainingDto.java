package com.epam.epamgymdemo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReporterTrainingDto {

    private String username;

    private String firstName;

    private String lastName;

    private Boolean active;

    private LocalDate trainingDate;

    private Double trainingDuration;

    private String actionType;
}
