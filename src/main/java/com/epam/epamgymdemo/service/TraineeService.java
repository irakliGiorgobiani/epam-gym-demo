package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.converter.BoToDtoConverter;
import com.epam.epamgymdemo.epamgymreporter.feignclient.ReporterClient;
import com.epam.epamgymdemo.exception.EntityNotFoundException;
import com.epam.epamgymdemo.metrics.CustomMetrics;
import com.epam.epamgymdemo.model.bo.Trainee;
import com.epam.epamgymdemo.model.bo.Trainer;
import com.epam.epamgymdemo.model.bo.Training;
import com.epam.epamgymdemo.model.bo.TrainingType;
import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.model.dto.ReporterTrainingDto;
import com.epam.epamgymdemo.model.dto.TraineeDto;
import com.epam.epamgymdemo.model.dto.TrainerDto;
import com.epam.epamgymdemo.model.dto.TraineeTrainingDto;
import com.epam.epamgymdemo.model.dto.UserDto;
import com.epam.epamgymdemo.repository.TraineeRepository;
import com.epam.epamgymdemo.repository.TrainingRepository;
import com.epam.epamgymdemo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class TraineeService {

    private final TraineeRepository traineeRepository;

    private final TrainingRepository trainingRepository;

    private final UserRepository userRepository;

    private final CustomMetrics customMetrics;

    private final ReporterClient reporterClient;

    private final BoToDtoConverter boToDtoConverter;

    @Transactional
    public void create(Long userId, TraineeDto traineeDto) {
        Trainee trainee = Trainee.builder()
                .birthday(traineeDto.getBirthday())
                .address(traineeDto.getAddress())
                .user(userRepository.findById(userId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                (String.format("User not found with the id: %d", userId)))))
                .build();

        traineeRepository.save(trainee);

        customMetrics.incrementTraineeCounter();

        log.info(String.format("Trainee with the id: %d successfully created", trainee.getId()));
    }

    @Transactional
    public TraineeDto update(String username, TraineeDto updatedTraineeDto) {
        Trainee trainee = this.getByUsername(username);

        if (updatedTraineeDto.getBirthday() != null) {
            trainee.setBirthday(updatedTraineeDto.getBirthday());
        }
        if (updatedTraineeDto.getAddress() != null) {
            trainee.setAddress(updatedTraineeDto.getAddress());
        }

        traineeRepository.save(trainee);

        log.info(String.format("Trainee with the id: %d successfully updated", trainee.getId()));

        return boToDtoConverter.traineeToTraineeDto(trainee, this.getTrainerList(trainee.getUser().getUsername()));
    }

    public TraineeDto get(String username) {
        Trainee trainee = this.getByUsername(username);

        return boToDtoConverter.traineeToTraineeDto(trainee, this.getTrainerList(trainee.getUser().getUsername()));
    }

    public Trainee getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException
                        (String.format("Trainee not found with the username: %s", username)));

        return user.getTrainee();
    }

    public List<Trainee> getAll() {
        return traineeRepository.findAll();
    }

    @Transactional
    public TraineeDto delete(String username) {
        Trainee trainee = this.getByUsername(username);
        TraineeDto traineeDto = boToDtoConverter.traineeToTraineeDto(trainee,
                this.getTrainerList(trainee.getUser().getUsername()));

        trainingRepository.findTrainings(username, null,
                null, null, null)
                .forEach(training -> {
            reporterClient.saveTraining(ReporterTrainingDto.builder()
                    .username(training.getTrainer().getUser().getUsername())
                    .firstName(training.getTrainer().getUser().getFirstName())
                    .lastName(training.getTrainer().getUser().getLastName())
                    .active(training.getTrainer().getUser().getIsActive())
                    .trainingDate(training.getTrainingDate())
                    .trainingDuration(training.getTrainingDuration().doubleValue())
                    .actionType("DELETE")
                    .build());

            trainingRepository.deleteById(training.getId());
        });

        userRepository.deleteById(trainee.getUser().getId());
        traineeRepository.deleteById(trainee.getId());

        customMetrics.decrementTraineeCounter();

        log.info(String.format("Trainee with the username: %s successfully deleted", username));

        return traineeDto;
    }

    public List<TraineeTrainingDto> getTrainingsByUsernameAndCriteria(String traineeUsername,
                                                                      LocalDate fromDate,
                                                                      LocalDate toDate,
                                                                      String trainerName,
                                                                      TrainingType trainingType) {

        List<Training> trainings = trainingRepository.findTrainings(traineeUsername, fromDate,
                toDate, trainerName, trainingType);

        return trainings.stream()
                .map(boToDtoConverter::trainingToTraineeTrainingDto)
                .toList();
    }

    public Set<TrainerDto> getUnassignedActiveTrainers(String username) {
        return getTrainerList(username).stream().filter(UserDto::getIsActive).collect(Collectors.toSet());
    }

    private Set<TrainerDto> getTrainerList(String username) {
        Trainee trainee = this.getByUsername(username);

        return trainee.getTrainers().stream()
                .map(boToDtoConverter::trainerToTrainerDto)
                .collect(Collectors.toSet());
    }

    @Transactional
    public Set<TrainerDto> addTrainerToTrainersList(String username, List<Trainer> trainers) {
        Trainee trainee = this.getByUsername(username);

        trainers.forEach(trainer -> {
            trainee.getTrainers().add(trainer);
            trainer.getTrainees().add(trainee);
        });

        log.info(String.format("Trainers have been added to the trainers list of trainee with the username: %s",
                username));

        return getTrainerList(username);
    }
}