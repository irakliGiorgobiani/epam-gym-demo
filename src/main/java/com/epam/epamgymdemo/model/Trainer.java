package com.epam.epamgymdemo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Trainer {
    private Long trainerId;
    private TrainingType specialization;
    private Long userId;
}
