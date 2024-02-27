package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.dao.TrainerDao;
import com.epam.epamgymdemo.dao.TrainingDao;
import com.epam.epamgymdemo.dao.TrainingTypeDao;
import com.epam.epamgymdemo.dao.UserDao;
import com.epam.epamgymdemo.generator.UsernamePasswordGenerator;
import com.epam.epamgymdemo.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
public class TrainerService {

    private final TrainerDao trainerDao;
    private final UserDao userDao;
    private final TrainingTypeDao trainingTypeDao;
    private final TrainingDao trainingDao;
    private final UsernamePasswordGenerator usernamePasswordGenerator;

    public TrainerService(TrainerDao trainerDao, UserDao userDao, TrainingTypeDao trainingTypeDao, TrainingDao trainingDao,
                          UsernamePasswordGenerator usernamePasswordGenerator) {
        this.trainerDao = trainerDao;
        this.userDao = userDao;
        this.trainingTypeDao = trainingTypeDao;
        this.trainingDao = trainingDao;
        this.usernamePasswordGenerator = usernamePasswordGenerator;
    }

    public void createTrainer(Long trainerId, Long typeId,
                              Long userId, String firstName, String lastName, Boolean isActive) throws InstanceNotFoundException {
        String username = usernamePasswordGenerator.generateUsername(firstName, lastName);
        String password = usernamePasswordGenerator.generatePassword();

        User user = User.builder()
                .id(userId)
                .firstName(firstName)
                .lastName(lastName)
                .userName(username)
                .password(password)
                .isActive(isActive)
                .build();
        userDao.create(user);

        log.info(String.format("User with the id: %d successfully created", userId));

        TrainingType specialization = trainingTypeDao.get(typeId);

        Trainer trainer = Trainer.builder()
                .id(trainerId)
                .trainingType(specialization)
                .user(userDao.get(userId))
                .build();
        trainerDao.create(trainer);

        log.info(String.format("Trainer with the id: %d successfully created", trainerId));
    }

    public void updateTrainer(Long id, Long typeId, Long userId) throws InstanceNotFoundException {
        TrainingType specialization = trainingTypeDao.get(typeId);

        Trainer trainer = Trainer.builder()
                .id(id)
                .trainingType(specialization)
                .user(userDao.get(userId))
                .build();
        trainerDao.update(trainer);

        log.info(String.format("Trainer with the id: %d successfully updated", id));
    }

    public Trainer selectTrainer(Long id) throws InstanceNotFoundException {
        return trainerDao.get(id);
    }

    public List<Trainer> selectAllTrainers() {
        return trainerDao.getAll();
    }

    public void changePassword(Long trainerId, String password) throws InstanceNotFoundException {
        var trainer = trainerDao.get(trainerId);
        userDao.updatePassword(trainer.getUser().getId(), password);
        log.info(String.format("Password for the trainer with the id: %d successfully changed", trainerId));
    }

    public void changeIsActive(Long trainerId, Boolean isActive) throws InstanceNotFoundException {
        var trainer = trainerDao.get(trainerId);
        userDao.updateIsActive(trainer.getUser().getId(), isActive);
        log.info(String.format("Activity for the trainer with the id: %d successfully changed", trainerId));
    }

    public Trainer getByUsername(String username) throws InstanceNotFoundException {
        User user = userDao.getByUsername(username);

        List<Trainer> trainers = trainerDao.getAll().stream().filter(t -> t.getUser().equals(user)).toList();

        if (trainers.size() == 0) {
            throw new InstanceNotFoundException(String.format("Trainer with not found with the username: %s", username));
        } else if (trainers.size() > 1) {
            throw new DuplicateKeyException(String.format("Multiple trainers found with the username : %s", username));
        } else return trainers.get(0);
    }

    public List<Training> getTrainingsByUsernameAndCriteria(String trainerUsername, LocalDate fromDate, LocalDate toDate,
                                                            String traineeName) throws InstanceNotFoundException {
        Trainer trainer = this.getByUsername(trainerUsername);

        Stream<Training> trainingStream = trainingDao.getAll().stream().filter(t -> t.getTrainer().equals(trainer));

        if (fromDate != null) {
            trainingStream = trainingStream.filter(t -> t.getTrainingDate().isEqual(fromDate) && t.getTrainingDate().isAfter(fromDate));
        }
        if (toDate != null) {
            trainingStream = trainingStream.filter(t -> t.getTrainingDate().isEqual(toDate) && t.getTrainingDate().isBefore(toDate));
        }
        if (traineeName != null) {
            trainingStream = trainingStream.filter(t -> t.getTrainee().getUser().getFirstName().equals(traineeName));
        }

        return trainingStream.toList();
    }
}
