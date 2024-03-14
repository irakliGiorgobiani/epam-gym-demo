package com.epam.epamgymdemo.service;


import com.epam.epamgymdemo.model.bo.Trainee;
import com.epam.epamgymdemo.model.bo.Trainer;
import com.epam.epamgymdemo.model.bo.Training;
import com.epam.epamgymdemo.model.bo.TrainingType;
import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.model.dto.TraineeDto;
import com.epam.epamgymdemo.model.dto.TraineeTrainingListDto;
import com.epam.epamgymdemo.repository.TraineeRepository;
import com.epam.epamgymdemo.repository.TrainerRepository;
import com.epam.epamgymdemo.repository.TrainingRepository;
import com.epam.epamgymdemo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@Service
@Slf4j
@AllArgsConstructor
public class TraineeService {

    private final TraineeRepository traineeRepository;

    private final TrainerRepository trainerRepository;

    private final TrainingRepository trainingRepository;

    private final UserRepository userRepository;

    private TraineeDto createDto(Trainee trainee) {
        return TraineeDto.builder()
                .birthday(trainee.getBirthday())
                .address(trainee.getAddress())
                .build();
    }

    @Transactional
    public void create(Long userId, TraineeDto traineeDto) throws InstanceNotFoundException {
        Trainee trainee = Trainee.builder()
                .birthday(traineeDto.getBirthday())
                .address(traineeDto.getAddress())
                .user(userRepository.findById(userId)
                        .orElseThrow(() -> new InstanceNotFoundException
                                (String.format("User not found with the id: %d", userId))))
                .build();

        traineeRepository.save(trainee);

        log.info(String.format("Trainee with the id: %d successfully created", trainee.getId()));
    }

    @Transactional
    public Map<String, Object> update(String username, TraineeDto updatedTraineeDto) throws InstanceNotFoundException {
        Trainee trainee = this.getByUsername(username);
        if (updatedTraineeDto.getBirthday() != null) {
            trainee.setBirthday(updatedTraineeDto.getBirthday());
        }
        if (updatedTraineeDto.getAddress() != null) {
            trainee.setAddress(updatedTraineeDto.getAddress());
        }

        traineeRepository.save(trainee);

        log.info(String.format("Trainee with the id: %d successfully updated", trainee.getId()));

        User user = trainee.getUser();

        return Map.of("username", user.getUsername(),
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "birthday", trainee.getBirthday(),
                "address", trainee.getAddress(),
                "isActive", user.getIsActive(),
                "trainersList", this.getTraineeTrainersList(trainee.getId()).stream()
                        .map(t -> Map.of("username", t.getUser().getUsername(),
                                "firstName", t.getUser().getFirstName(),
                                "lastName", t.getUser().getLastName(),
                                "specialization", t.getTrainingType().getId()))
                        .toList());
    }

    public Map<String, Object> get(String username) throws InstanceNotFoundException {
        Trainee trainee = this.getByUsername(username);
        User user = trainee.getUser();

        return Map.of("firstName", user.getFirstName(), "lastName", user.getLastName(),
                "dateOfBirth", trainee.getBirthday(), "address", trainee.getAddress(),
                "isActive", user.getIsActive(), "trainersList", trainee.getTrainers().stream()
                        .map(t -> Map.of("username", t.getUser().getUsername(), "firstName", t.getUser().getFirstName(),
                                "lastName", t.getUser().getLastName(), "specialization", t.getTrainingType().getId()))
                        .toList());
    }

    public Trainee getById(Long id) throws InstanceNotFoundException {
        return traineeRepository.findById(id)
                .orElseThrow(() -> new InstanceNotFoundException
                        (String.format("Trainee not found with the id: %d", id)));
    }

    public Trainee getByUsername(String username) throws InstanceNotFoundException {
        User user = userRepository.findByUsername(username);

        List<Trainee> trainees = this.getAll().stream().filter(t -> t.getUser().equals(user)).toList();

        if (trainees.size() == 0) {
            throw new InstanceNotFoundException(String.format("Trainee not found with the username: %s", username));
        } else if (trainees.size() > 1) {
            throw new DuplicateKeyException(String.format("Multiple trainees found with the username : %s", username));
        } else return trainees.get(0);
    }

    public List<Trainee> getAll() {
        return traineeRepository.findAll();
    }

    public Long getUserId(Long id) throws InstanceNotFoundException {
        return this.getById(id).getUser().getId();
    }

    @Transactional
    public TraineeDto deleteById(Long id) throws InstanceNotFoundException {
        Trainee trainee = this.getById(id);

        userRepository.deleteById(this.getById(id).getUser().getId());
        traineeRepository.deleteById(id);

        log.info(String.format("Trainee with the id: %d successfully deleted", id));

        return createDto(trainee);
    }

    @Transactional
    public TraineeDto deleteByUsername(String username) throws InstanceNotFoundException {
        TraineeDto traineeDto = this.deleteById(this.getByUsername(username).getId());

        log.info(String.format("Trainee with the username: %s successfully deleted", username));

        return traineeDto;
    }

    public List<TraineeTrainingListDto> getTrainingsByUsernameAndCriteria(String traineeUsername, LocalDate fromDate,
                                                                          LocalDate toDate,
                                                                          String trainerName, TrainingType trainingType)
            throws InstanceNotFoundException {
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

        List<TraineeTrainingListDto> trainerListDtos = new ArrayList<>();

        for (Training training : trainings) {
            trainerListDtos.add(TraineeTrainingListDto.builder()
                    .trainingName(training.getTrainingName())
                    .trainingDate(training.getTrainingDate())
                    .trainingDuration(training.getTrainingDuration())
                    .typeName(training.getTrainingType().getTypeName())
                    .trainerName(training.getTrainer().getUser().getFirstName())
                    .build());
        }

        return trainerListDtos;
    }

    private Map<Long, Map<String, Object>> getTrainerList(List<Trainer> trainers) {
        Map<Long, Map<String, Object>> result = new HashMap<>();

        for (Trainer trainer : trainers) {
            result.put(trainer.getId(), Map.of("username", trainer.getUser().getUsername(),
                    "firstName", trainer.getUser().getFirstName(),
                    "lastName", trainer.getUser().getLastName(),
                    "specialization", trainer.getTrainingType().getId()));
        }

        return result;
    }

    public Map<Long, Map<String, Object>> getUnassignedActiveTrainers(String username) throws InstanceNotFoundException {
        Trainee trainee = this.getByUsername(username);

        List<Trainer> trainers = trainerRepository.findAll().stream()
                .filter(t -> !trainee.getTrainers().contains(t) && t.getUser().getIsActive()).toList();

        return getTrainerList(trainers);
    }

    public Set<Trainer> getTraineeTrainersList(Long id) throws InstanceNotFoundException {
        Trainee trainee = this.getById(id);

        return trainee.getTrainers();
    }

    @Transactional
    public Map<Long, Map<String, Object>> addTrainerToTrainersList(String username, List<Trainer> trainers) throws InstanceNotFoundException {
        Trainee trainee = this.getByUsername(username);

        for(Trainer trainer : trainers) {
            trainee.getTrainers().add(trainer);
            trainer.getTrainees().add(trainee);
        }

        log.info(String.format("Trainers have been added to the trainers list of trainee with the username: %s", username));

        return getTrainerList(trainers);
    }

    @Transactional
    public void deleteTrainerFromTrainersList(Long id, Trainer trainer) throws InstanceNotFoundException {
        Trainee trainee = this.getById(id);

        trainee.getTrainers().remove(trainer);
        trainer.getTrainees().remove(trainee);

        log.info(String.format("Trainer with the id: %d has been removed from the trainers list of trainee with the id: %d", trainer.getId(), id));
    }
}
