package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.exception.MissingInstanceException;
import com.epam.epamgymdemo.model.bo.Trainee;
import com.epam.epamgymdemo.model.bo.Trainer;
import com.epam.epamgymdemo.model.bo.Training;
import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.model.dto.TrainingDto;
import com.epam.epamgymdemo.repository.TrainingRepository;
import com.epam.epamgymdemo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;

    private final UserRepository userRepository;

    @Transactional
    public void create(TrainingDto trainingDto) {
        Trainee trainee = getUserByUsername(trainingDto.getTraineeUsername()).getTrainee();
        Trainer trainer = getUserByUsername(trainingDto.getTrainerUsername()).getTrainer();

        Training training = Training.builder()
                .trainingName(trainingDto.getTrainingName())
                .trainingDate(trainingDto.getTrainingDate())
                .trainingDuration(trainingDto.getTrainingDuration())
                .trainee(trainee)
                .trainer(trainer)
                .trainingType(trainer.getTrainingType())
                .build();

        trainingRepository.save(training);

        log.info(String.format("Training with the id: %d successfully created", training.getId()));

        trainee.getTrainers().add(trainer);
        trainer.getTrainees().add(trainee);
    }

    public List<Training> getAll() {
        return trainingRepository.findAll();
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new MissingInstanceException
                        (String.format("User not found with the username: %s", username)));
    }
}