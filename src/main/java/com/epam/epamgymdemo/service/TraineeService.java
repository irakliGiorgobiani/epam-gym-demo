package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.model.*;
import com.epam.epamgymdemo.repository.TraineeRepository;
import com.epam.epamgymdemo.repository.TrainerRepository;
import com.epam.epamgymdemo.repository.TrainingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@AllArgsConstructor
public class TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final UserService userService;

    public void create(LocalDate birthday, String address,
                       String firstName, String lastName, Boolean isActive) throws InstanceNotFoundException {
        User user = userService.create(firstName, lastName, isActive);

        Trainee trainee = Trainee.builder()
                .birthday(birthday)
                .address(address)
                .user(user)
                .trainers(new HashSet<>())
                .build();
        traineeRepository.save(trainee);

        log.info(String.format("Trainee with the id: %d successfully created", trainee.getId()));
    }

    public void update(Long id, LocalDate birthday, String address, Long userId) throws InstanceNotFoundException {
        Trainee trainee = this.getById(id);
        if (birthday != null) {
            trainee.setBirthday(birthday);
        }
        if (address != null) {
            trainee.setAddress(address);
        }
        if (userId != null) {
            trainee.setUser(userService.getById(userId));
        }

        traineeRepository.save(trainee);

        log.info(String.format("Trainee with the id: %d successfully updated", id));
    }

    public Trainee getById(Long id) throws InstanceNotFoundException {
        return traineeRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException(String.format("Trainee not found with the id: %d", id)));
    }

    public List<Trainee> getAll() {
        return traineeRepository.findAll();
    }

    public void deleteById(Long id) {
        traineeRepository.deleteById(id);
        log.info(String.format("Trainee with the id: %d successfully deleted", id));
    }

    public void changePassword(Long id, String password) throws InstanceNotFoundException {
        var trainee = this.getById(id);

        userService.changePassword(trainee.getId(), password);

        log.info(String.format("Password for the trainee with the id: %d successfully changed", id));
    }

    public void changeIsActive(Long id, Boolean isActive) throws InstanceNotFoundException {
        var trainee = this.getById(id);

        userService.changeIsActive(trainee.getId(), isActive);

        log.info(String.format("Activity for the trainee with the id: %d successfully changed", id));
    }

    public Trainee getByUsername(String username) throws InstanceNotFoundException {
        User user = userService.getByUsername(username);

        List<Trainee> trainees = this.getAll().stream().filter(t -> t.getUser().equals(user)).toList();

        if (trainees.size() == 0) {
            throw new InstanceNotFoundException(String.format("Trainee not found with the username: %s", username));
        } else if (trainees.size() > 1) {
            throw new DuplicateKeyException(String.format("Multiple trainees found with the username : %s", username));
        } else return trainees.get(0);
    }

    public void deleteByUsername(String username) throws InstanceNotFoundException {
        Long id = userService.getByUsername(username).getId();

        this.deleteById(id);

        log.info(String.format("Trainee with the username: %s successfully deleted", username));
    }

    public List<Training> getTrainingsByUsernameAndCriteria(String traineeUsername, LocalDate fromDate, LocalDate toDate,
                                                            String trainerName, TrainingType trainingType) throws InstanceNotFoundException {
        Trainee trainee = this.getByUsername(traineeUsername);

        Stream<Training> trainingStream = trainingRepository.findAll().stream().filter(t -> t.getTrainee().equals(trainee));

        if (fromDate != null) {
            trainingStream = trainingStream.filter(t -> t.getTrainingDate().isEqual(fromDate) || t.getTrainingDate().isAfter(fromDate));
        }
        if (toDate != null) {
            trainingStream = trainingStream.filter(t -> t.getTrainingDate().isEqual(toDate) || t.getTrainingDate().isBefore(toDate));
        }
        if (trainerName != null) {
            trainingStream = trainingStream.filter(t -> t.getTrainer().getUser().getFirstName().equals(trainerName));
        }
        if (trainingType != null) {
            trainingStream = trainingStream.filter(t -> t.getTrainingType().getId().longValue() == trainingType.getId().longValue());
        }

        return trainingStream.toList();
    }

    public List<Trainer> getUnassignedTrainers(String username) throws InstanceNotFoundException {
        Trainee trainee = this.getByUsername(username);

        return trainerRepository.findAll().stream().filter(t -> !trainee.getTrainers().contains(t)).toList();
    }

    public void addTrainerToTrainersList(Long id, Trainer trainer) throws InstanceNotFoundException {
        Trainee trainee = this.getById(id);

        trainee.getTrainers().add(trainer);
        trainer.getTrainees().add(trainee);

        log.info(String.format("Trainer with the id: %d has been added to the trainers list of trainee with the id: %d", trainer.getId(), id));
    }

    public void removeTrainerFromTrainersList(Long id, Trainer trainer) throws InstanceNotFoundException {
        Trainee trainee = this.getById(id);

        trainee.getTrainers().remove(trainer);
        trainer.getTrainees().remove(trainee);

        log.info(String.format("Trainer with the id: %d has been removed from the trainers list of trainee with the id: %d", trainer.getId(), id));
    }
}
