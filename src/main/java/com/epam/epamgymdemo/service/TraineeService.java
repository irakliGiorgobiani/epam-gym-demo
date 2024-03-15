package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.exception.MissingInstanceException;
import com.epam.epamgymdemo.model.bo.Trainee;
import com.epam.epamgymdemo.model.bo.Trainer;
import com.epam.epamgymdemo.model.bo.Training;
import com.epam.epamgymdemo.model.bo.TrainingType;
import com.epam.epamgymdemo.model.bo.User;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@AllArgsConstructor
public class TraineeService {

    private final TraineeRepository traineeRepository;

    private final TrainingRepository trainingRepository;

    private final UserRepository userRepository;

    private TraineeDto createTraineeDto(Trainee trainee) {
        return TraineeDto.builder()
                .firstName(trainee.getUser().getFirstName())
                .lastName(trainee.getUser().getLastName())
                .username(trainee.getUser().getUsername())
                .birthday(trainee.getBirthday())
                .address(trainee.getAddress())
                .isActive(trainee.getUser().getIsActive())
                .trainers(this.getTrainerList(trainee.getUser().getUsername()))
                .build();
    }

    @Transactional
    public void create(Long userId, TraineeDto traineeDto) {
        Trainee trainee = Trainee.builder()
                .birthday(traineeDto.getBirthday())
                .address(traineeDto.getAddress())
                .user(userRepository.findById(userId)
                        .orElseThrow(() -> new MissingInstanceException(
                                (String.format("User not found with the id: %d", userId)))))
                .build();

        traineeRepository.save(trainee);

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

        return createTraineeDto(trainee);
    }

    public TraineeDto get(String username) {
        Trainee trainee = this.getByUsername(username);

        return createTraineeDto(trainee);
    }

    public Trainee getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new MissingInstanceException
                        (String.format("Trainee not found with the username: %s", username)));

        return user.getTrainee();
    }

    public List<Trainee> getAll() {
        return traineeRepository.findAll();
    }

    @Transactional
    public TraineeDto deleteByUsername(String username) {
        Trainee trainee = this.getByUsername(username);
        TraineeDto traineeDto = createTraineeDto(trainee);

        userRepository.deleteById(trainee.getUser().getId());
        traineeRepository.deleteById(trainee.getId());

        log.info(String.format("Trainee with the username: %s successfully deleted", username));

        return traineeDto;
    }

    public List<TraineeTrainingDto> getTrainingsByUsernameAndCriteria(String traineeUsername,
                                                                      LocalDate fromDate,
                                                                      LocalDate toDate,
                                                                      String trainerName,
                                                                      TrainingType trainingType) {
        Trainee trainee = this.getByUsername(traineeUsername);

        Stream<Training> trainingStream = trainingRepository.findAll().stream()
                .filter(t -> t.getTrainee().equals(trainee));

        if (fromDate != null) {
            trainingStream = trainingStream.filter(t -> t.getTrainingDate()
                    .isEqual(fromDate) || t.getTrainingDate().isAfter(fromDate));
        }
        if (toDate != null) {
            trainingStream = trainingStream.filter(t -> t.getTrainingDate()
                    .isEqual(toDate) || t.getTrainingDate().isBefore(toDate));
        }
        if (trainerName != null) {
            trainingStream = trainingStream.filter(t -> t.getTrainer().getUser().getFirstName().equals(trainerName));
        }
        if (trainingType != null) {
            trainingStream = trainingStream.filter(t -> t.getTrainingType()
                    .getId().longValue() == trainingType.getId().longValue());
        }

        List<Training> trainings = trainingStream.toList();

        List<TraineeTrainingDto> traineeTrainingDtoList = new ArrayList<>();

        for (Training training : trainings) {
            traineeTrainingDtoList.add(TraineeTrainingDto.builder()
                    .trainingName(training.getTrainingName())
                    .trainingDate(training.getTrainingDate())
                    .trainingDuration(training.getTrainingDuration())
                    .trainingType(training.getTrainingType().getTypeName())
                    .trainerName(training.getTrainer().getUser().getFirstName() + " "
                            + training.getTrainer().getUser().getLastName())
                    .build());
        }

        return traineeTrainingDtoList;
    }

    public Set<TrainerDto> getUnassignedActiveTrainers(String username) {
        return getTrainerList(username).stream().filter(UserDto::getIsActive).collect(Collectors.toSet());
    }

    private Set<TrainerDto> getTrainerList(String username) {
        Trainee trainee = this.getByUsername(username);

        return trainee.getTrainers().stream()
                .map(t -> TrainerDto.builder()
                        .specializationId(t.getTrainingType().getId())
                        .isActive(t.getUser().getIsActive())
                        .firstName(t.getUser().getFirstName())
                        .lastName(t.getUser().getLastName())
                        .username(t.getUser().getUsername())
                        .build())
                .collect(Collectors.toSet());
    }

    @Transactional
    public Set<TrainerDto> addTrainerToTrainersList(String username, List<Trainer> trainers) {
        Trainee trainee = this.getByUsername(username);

        for(Trainer trainer : trainers) {
            trainee.getTrainers().add(trainer);
            trainer.getTrainees().add(trainee);
        }

        log.info(String.format("Trainers have been added to the trainers list of trainee with the username: %s",
                username));

        return getTrainerList(username);
    }
}