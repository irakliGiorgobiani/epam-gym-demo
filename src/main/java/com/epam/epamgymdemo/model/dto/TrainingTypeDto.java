package com.epam.epamgymdemo.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class TrainingTypeDto {

    private Long id;

    private String typeName;

    private final List<TrainerDto> trainers = new ArrayList<>();

    private final List<TrainingDto> trainings = new ArrayList<>();
}
