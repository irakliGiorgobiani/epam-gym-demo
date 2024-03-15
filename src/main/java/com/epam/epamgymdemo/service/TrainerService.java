package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.exception.MissingInstanceException;
import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.model.bo.TrainingType;
import com.epam.epamgymdemo.model.bo.Trainer;
import com.epam.epamgymdemo.model.bo.Training;
import com.epam.epamgymdemo.model.dto.TrainerTrainingDto;
import com.epam.epamgymdemo.model.dto.TrainerWithListDto;
import com.epam.epamgymdemo.model.dto.UserDto;
import com.epam.epamgymdemo.repository.TrainerRepository;
import com.epam.epamgymdemo.repository.TrainingRepository;
import com.epam.epamgymdemo.repository.TrainingTypeRepository;
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
public class TrainerService {

    private final TrainerRepository trainerRepository;

    private final TrainingRepository trainingRepository;

    private final TrainingTypeRepository trainingTypeRepository;

    private final UserRepository userRepository;

    private TrainerWithListDto createTrainerWithListDto(Trainer trainer) {
        return TrainerWithListDto.builder()
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .isActive(trainer.getUser().getIsActive())
                .specializationId(trainer.getTrainingType().getId())
                .username(trainer.getUser().getUsername())
                .trainees(this.getTraineeList(trainer.getUser().getUsername()))
                .build();
    }

    @Transactional
    public void create(Long userId, Long typeId) {
        TrainingType specialization = trainingTypeRepository.findById(typeId)
                .orElseThrow(() -> new MissingInstanceException
                        (String.format("Training type not found with the id: %d", typeId)));

        Trainer trainer = Trainer.builder()
                .trainingType(specialization)
                .user(userRepository.findById(userId).orElseThrow(() -> new MissingInstanceException
                        (String.format("User not found with the id: %d", userId))))
                .build();

        trainerRepository.save(trainer);

        log.info(String.format("Trainer with the id: %d successfully created", trainer.getId()));
    }

    @Transactional
    public TrainerWithListDto update(String username) {
        Trainer trainer = this.getByUsername(username);

        log.info(String.format("Trainer with the id: %d successfully updated", trainer.getId()));

        return createTrainerWithListDto(trainer);
    }

    public TrainerWithListDto get(String username) {
        Trainer trainer = this.getByUsername(username);

        return createTrainerWithListDto(trainer);
    }

    public Trainer getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new MissingInstanceException
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
        Trainer trainer = this.getByUsername(trainerUsername);

        Stream<Training> trainingStream = trainingRepository.findAll().stream()
                .filter(t -> t.getTrainer().equals(trainer));

        if (fromDate != null) {
            trainingStream = trainingStream.filter(t -> t.getTrainingDate()
                    .isEqual(fromDate) || t.getTrainingDate().isAfter(fromDate));
        }
        if (toDate != null) {
            trainingStream = trainingStream.filter(t -> t.getTrainingDate()
                    .isEqual(toDate) || t.getTrainingDate().isBefore(toDate));
        }
        if (traineeName != null) {
            trainingStream = trainingStream
                    .filter(t -> t.getTrainee().getUser().getFirstName().equals(traineeName));
        }

        List<Training> trainings = trainingStream.toList();

        List<TrainerTrainingDto> traineeTrainingDtoList = new ArrayList<>();

        for (Training training : trainings) {
            traineeTrainingDtoList.add(TrainerTrainingDto.builder()
                    .trainingName(training.getTrainingName())
                    .trainingDate(training.getTrainingDate())
                    .trainingDuration(training.getTrainingDuration())
                    .trainingType(training.getTrainingType().getTypeName())
                    .traineeName(training.getTrainee().getUser().getFirstName() + " "
                            + training.getTrainee().getUser().getLastName())
                    .build());
        }

        return traineeTrainingDtoList;
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
}