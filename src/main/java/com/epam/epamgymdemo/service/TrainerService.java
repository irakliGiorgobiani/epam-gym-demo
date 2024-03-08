package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.model.User;
import com.epam.epamgymdemo.model.TrainingType;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.repository.TrainerRepository;
import com.epam.epamgymdemo.repository.TrainingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@AllArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final TrainingTypeService trainingTypeService;
    private final UserService userService;

    @Transactional
    public Trainer create(Long typeId, String firstName, String lastName, Boolean isActive) throws InstanceNotFoundException {
        User user = userService.create(firstName, lastName, isActive);
        TrainingType specialization = trainingTypeService.getById(typeId);

        Trainer trainer = Trainer.builder()
                .trainingType(specialization)
                .user(user)
                .build();
        trainerRepository.save(trainer);

        log.info(String.format("Trainer with the id: %d successfully created", trainer.getId()));

        return trainer;
    }

    @Transactional
    public void update(Long id, Long typeId, Long userId) throws InstanceNotFoundException {
        Trainer trainer = this.getById(id);

        if (typeId != null) {
            trainer.setTrainingType(trainingTypeService.getById(typeId));
        }
        if (userId != null) {
            trainer.setUser(userService.getById(userId));
        }

        trainerRepository.save(trainer);

        log.info(String.format("Trainer with the id: %d successfully updated", id));
    }

    public Trainer getById(Long id) throws InstanceNotFoundException {
        return trainerRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException(String.format("Trainer not found with the id: %d", id)));
    }

    public List<Trainer> getAll() {
        return trainerRepository.findAll();
    }

    @Transactional
    public void changePassword(Long id, String password) throws InstanceNotFoundException {
        var trainer = this.getById(id);

        userService.changePassword(trainer.getUser().getId(), password);

        log.info(String.format("Password for the trainer with the id: %d successfully changed", id));
    }

    @Transactional
    public void changeIsActive(Long id, Boolean isActive) throws InstanceNotFoundException {
        var trainer = this.getById(id);

        userService.changeIsActive(trainer.getUser().getId(), isActive);

        log.info(String.format("Activity for the trainer with the id: %d successfully changed", id));
    }

    public Trainer getByUsername(String username) throws InstanceNotFoundException {
        User user = userService.getByUsername(username);

        List<Trainer> trainers = trainerRepository.findAll().stream().filter(t -> t.getUser().equals(user)).toList();

        if (trainers.size() == 0) {
            throw new InstanceNotFoundException(String.format("Trainer with not found with the username: %s", username));
        } else if (trainers.size() > 1) {
            throw new DuplicateKeyException(String.format("Multiple trainers found with the username : %s", username));
        } else return trainers.get(0);
    }

    public List<Training> getTrainingsByUsernameAndCriteria(String trainerUsername, LocalDate fromDate, LocalDate toDate,
                                                            String traineeName) throws InstanceNotFoundException {
        Trainer trainer = this.getByUsername(trainerUsername);

        Stream<Training> trainingStream = trainingRepository.findAll().stream().filter(t -> t.getTrainer().equals(trainer));

        if (fromDate != null) {
            trainingStream = trainingStream.filter(t -> t.getTrainingDate().isEqual(fromDate) || t.getTrainingDate().isAfter(fromDate));
        }
        if (toDate != null) {
            trainingStream = trainingStream.filter(t -> t.getTrainingDate().isEqual(toDate) || t.getTrainingDate().isBefore(toDate));
        }
        if (traineeName != null) {
            trainingStream = trainingStream.filter(t -> t.getTrainee().getUser().getFirstName().equals(traineeName));
        }

        return trainingStream.toList();
    }

    @Transactional
    public void changeUsername(Long id, String username) throws InstanceNotFoundException {
        var trainer = this.getById(id);

        userService.changeUsername(trainer.getUser().getId(), username);

        log.info(String.format("Username for the trainer with the id: %d successfully changed", id));
    }

    @Transactional
    public void changeFirstName(Long id, String firstName) throws InstanceNotFoundException {
        var trainer = this.getById(id);

        userService.changeFirstName(trainer.getUser().getId(), firstName);

        log.info(String.format("First name for the trainee with the id: %d successfully changed", id));
    }

    @Transactional
    public void changeLastName(Long id, String lastName) throws InstanceNotFoundException {
        var trainer = this.getById(id);

        userService.changeLastName(trainer.getUser().getId(), lastName);

        log.info(String.format("Last name for the trainee with the id: %d successfully changed", id));
    }
}
