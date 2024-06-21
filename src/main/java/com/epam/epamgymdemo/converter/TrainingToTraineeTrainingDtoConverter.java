package com.epam.epamgymdemo.converter;

import com.epam.epamgymdemo.model.bo.Training;
import com.epam.epamgymdemo.model.dto.TraineeTrainingDto;
import org.springframework.stereotype.Component;

@Component
public class TrainingToTraineeTrainingDtoConverter {

    public TraineeTrainingDto convert(Training training) {
        return TraineeTrainingDto.builder()
                .trainingName(training.getTrainingName())
                .trainingDate(training.getTrainingDate())
                .trainingDuration(training.getTrainingDuration())
                .trainingType(training.getTrainingType().getTypeName())
                .trainerName(training.getTrainer().getUser().getFirstName() + " "
                        + training.getTrainer().getUser().getLastName())
                .build();
    }
}
