package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.exception.EntityNotFoundException;
import com.epam.epamgymdemo.epamgymreporter.messaging.ReporterTrainingProducer;
import com.epam.epamgymdemo.model.bo.Trainee;
import com.epam.epamgymdemo.model.bo.Trainer;
import com.epam.epamgymdemo.model.bo.Training;
import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.model.dto.ReporterTrainingDto;
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

    private final ReporterTrainingProducer reporterTrainingProducer;

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException
                        (String.format("User not found with the username: %s", username)));
    }

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

        reporterTrainingProducer.send(ReporterTrainingDto.builder()
                .username(trainer.getUser().getUsername())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .isActive(trainer.getUser().getIsActive())
                .trainingDate(training.getTrainingDate())
                .trainingDuration(training.getTrainingDuration())
                .build());
    }

    public List<Training> getAll() {
        return trainingRepository.findAll();
    }
}