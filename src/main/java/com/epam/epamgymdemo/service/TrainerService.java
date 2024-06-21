package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.converter.TrainerToTrainerWithListDtoConverter;
import com.epam.epamgymdemo.exception.EntityNotFoundException;
import com.epam.epamgymdemo.metrics.CustomMetrics;
import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.model.bo.TrainingType;
import com.epam.epamgymdemo.model.bo.Trainer;
import com.epam.epamgymdemo.model.bo.Training;
import com.epam.epamgymdemo.model.dto.TrainerTrainingDto;
import com.epam.epamgymdemo.model.dto.TrainerWithListDto;
import com.epam.epamgymdemo.model.dto.TrainingSummaryDto;
import com.epam.epamgymdemo.model.dto.UserDto;
import com.epam.epamgymdemo.repository.TrainerRepository;
import com.epam.epamgymdemo.repository.TrainingRepository;
import com.epam.epamgymdemo.repository.TrainingTypeRepository;
import com.epam.epamgymdemo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepository;

    private final TrainingRepository trainingRepository;

    private final TrainingTypeRepository trainingTypeRepository;

    private final UserRepository userRepository;

    private final CustomMetrics customMetrics;

    private final TrainerToTrainerWithListDtoConverter trainerToTrainerWithListDtoConverter;

    private final ObjectMapper objectMapper;

    @Transactional
    public void create(Long userId, Long typeId) {
        TrainingType specialization = trainingTypeRepository.findById(typeId)
                .orElseThrow(() -> new EntityNotFoundException
                        (String.format("Training type not found with the id: %d", typeId)));

        Trainer trainer = Trainer.builder()
                .trainingType(specialization)
                .user(userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException
                        (String.format("User not found with the id: %d", userId))))
                .build();

        trainerRepository.save(trainer);

        customMetrics.incrementTrainerCounter();

        log.info(String.format("Trainer with the id: %d successfully created", trainer.getId()));
    }

    @Transactional
    public TrainerWithListDto update(String username) {
        Trainer trainer = this.getByUsername(username);

        log.info(String.format("Trainer with the id: %d successfully updated", trainer.getId()));

        return trainerToTrainerWithListDtoConverter.convert(trainer,
                this.getTraineeList(trainer.getUser().getUsername()));
    }

    public TrainerWithListDto get(String username) {
        Trainer trainer = this.getByUsername(username);

        return trainerToTrainerWithListDtoConverter.convert(trainer,
                this.getTraineeList(trainer.getUser().getUsername()));
    }

    public Trainer getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException
                        (String.format("Trainer not found with the username: %s", username)));

        return user.getTrainer();
    }

    public List<Trainer> getByUsernames(List<String> usernames) {
        List<Trainer> trainers = new ArrayList<>();

        for (String username : usernames) {
            trainers.add(this.getByUsername(username));
        }

        return trainers;
    }

    public List<Trainer> getAll() {
        return trainerRepository.findAll();
    }

    public List<TrainerTrainingDto> getTrainingsByUsernameAndCriteria(String trainerUsername,
                                                                      LocalDate fromDate,
                                                                      LocalDate toDate,
                                                                      String traineeName) {
        List<Training> trainings = trainingRepository.findTrainings(trainerUsername, fromDate, toDate, traineeName);

        return trainings.stream()
                .map(training -> TrainerTrainingDto.builder()
                        .trainingName(training.getTrainingName())
                        .trainingDate(training.getTrainingDate())
                        .trainingDuration(training.getTrainingDuration())
                        .trainingType(training.getTrainingType().getTypeName())
                        .traineeName(training.getTrainee().getUser().getFirstName() + " "
                                + training.getTrainee().getUser().getLastName())
                        .build())
                .toList();
    }

    public Set<UserDto> getTraineeList(String username) {
        Trainer trainer = this.getByUsername(username);

        return trainer.getTrainees().stream()
                .map(t -> UserDto.builder()
                        .firstName(t.getUser().getFirstName())
                        .lastName(t.getUser().getLastName())
                        .username(t.getUser().getUsername())
                        .isActive(t.getUser().getIsActive())
                        .build())
                .collect(Collectors.toSet());
    }

    public TrainingSummaryDto processMonthlySummary(TrainingSummaryDto trainingSummaryDto) {
        if(trainingSummaryDto != null) {
            try {
                return objectMapper.readValue(((TextMessage) trainingSummaryDto).getText(),
                        TrainingSummaryDto.class);
            } catch (JMSException | IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else throw new RuntimeException("Something went wrong, please try again");
    }
}