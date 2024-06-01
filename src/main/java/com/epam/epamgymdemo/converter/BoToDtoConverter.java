package com.epam.epamgymdemo.converter;

import com.epam.epamgymdemo.model.bo.Trainee;
import com.epam.epamgymdemo.model.bo.Trainer;
import com.epam.epamgymdemo.model.bo.Training;
import com.epam.epamgymdemo.model.dto.TraineeDto;
import com.epam.epamgymdemo.model.dto.TraineeTrainingDto;
import com.epam.epamgymdemo.model.dto.TrainerDto;
import com.epam.epamgymdemo.model.dto.TrainerWithListDto;
import com.epam.epamgymdemo.model.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class BoToDtoConverter {

    public TraineeDto traineeToTraineeDto(Trainee trainee, Set<TrainerDto> trainerSet) {
        return TraineeDto.builder()
                .firstName(trainee.getUser().getFirstName())
                .lastName(trainee.getUser().getLastName())
                .username(trainee.getUser().getUsername())
                .birthday(trainee.getBirthday())
                .address(trainee.getAddress())
                .isActive(trainee.getUser().getIsActive())
                .trainers(trainerSet)
                .build();
    }

    public TrainerDto trainerToTrainerDto(Trainer trainer) {
        return TrainerDto.builder()
                .specializationId(trainer.getTrainingType().getId())
                .isActive(trainer.getUser().getIsActive())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .username(trainer.getUser().getUsername())
                .build();
    }

    public TraineeTrainingDto trainingToTraineeTrainingDto(Training training) {
        return TraineeTrainingDto.builder()
                .trainingName(training.getTrainingName())
                .trainingDate(training.getTrainingDate())
                .trainingDuration(training.getTrainingDuration())
                .trainingType(training.getTrainingType().getTypeName())
                .trainerName(training.getTrainer().getUser().getFirstName() + " "
                        + training.getTrainer().getUser().getLastName())
                .build();
    }

    public TrainerWithListDto trainerToTrainerWithListDto(Trainer trainer, Set<UserDto> traineeSet) {
        return TrainerWithListDto.builder()
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .isActive(trainer.getUser().getIsActive())
                .specializationId(trainer.getTrainingType().getId())
                .username(trainer.getUser().getUsername())
                .trainees(traineeSet)
                .build();
    }
}
