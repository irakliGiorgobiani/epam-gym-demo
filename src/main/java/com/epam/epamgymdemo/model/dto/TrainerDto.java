package com.epam.epamgymdemo.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Data
@Builder
public class TrainerDto {

    private Long id;

    private TrainingTypeDto trainingType;

    private UserDto user;

    private final List<TrainingDto> trainings = new ArrayList<>();

    private Set<TraineeDto> trainees;
}
