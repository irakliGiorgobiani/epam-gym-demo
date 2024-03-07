package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.dao.TrainerDao;
import com.epam.epamgymdemo.dao.TrainingDao;
import com.epam.epamgymdemo.dao.UserDao;
import com.epam.epamgymdemo.generator.UsernamePasswordGenerator;
import com.epam.epamgymdemo.model.*;
import com.epam.epamgymdemo.dao.TraineeDao;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
@Slf4j
public class TraineeService {

    private final TraineeDao traineeDao;
    private final UserDao userDao;
    private final TrainingDao trainingDao;
    private final TrainerDao trainerDao;
    private final UsernamePasswordGenerator usernamePasswordGenerator;

    public TraineeService(TraineeDao traineeDao, UserDao userDao, TrainingDao trainingDao, TrainerDao trainerDao,
                          UsernamePasswordGenerator usernamePasswordGenerator) {
        this.traineeDao = traineeDao;
        this.userDao = userDao;
        this.trainingDao =trainingDao;
        this.trainerDao = trainerDao;
        this.usernamePasswordGenerator = usernamePasswordGenerator;
    }

    static User userCreation(String firstName, String lastName, Boolean isActive, UsernamePasswordGenerator usernamePasswordGenerator, UserDao userDao, Logger log) {
        String username = usernamePasswordGenerator.generateUsername(firstName, lastName);
        String password = usernamePasswordGenerator.generatePassword();

        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .userName(username)
                .password(password)
                .isActive(isActive)
                .build();
        userDao.create(user);

        log.info(String.format("User with the id: %d successfully created", user.getId()));

        return user;
    }

    public Trainee createTrainee(LocalDate dateOfBirth, String address,
                              String firstName, String lastName, Boolean isActive) {
        User user = userCreation(firstName, lastName, isActive, usernamePasswordGenerator, userDao, log);

        Trainee trainee = Trainee.builder()
                .dateOfBirth(dateOfBirth)
                .address(address)
                .user(user)
                .trainers(new HashSet<>())
                .build();
        traineeDao.create(trainee);

        log.info(String.format("Trainee with the id: %d successfully created", trainee.getId()));

        return trainee;
    }

    public void updateTrainee(Long id, LocalDate dateOfBirth, String address, Long userId) throws InstanceNotFoundException {
        Trainee trainee = traineeDao.get(id);
        if (dateOfBirth != null) {
            trainee.setDateOfBirth(dateOfBirth);
        }
        if (address != null) {
            trainee.setAddress(address);
        }
        if (userId != null) {
            trainee.setUser(userDao.get(userId));
        }

        traineeDao.update(trainee);

        log.info(String.format("Trainee with the id: %d successfully updated", id));
    }

    public Trainee selectTrainee(Long id) throws InstanceNotFoundException {
        return traineeDao.get(id);
    }

    public List<Trainee> selectAllTrainees() {
        return traineeDao.getAll();
    }

    public void deleteTrainee(Long id) throws InstanceNotFoundException {
        Trainee trainee = traineeDao.get(id);
        userDao.delete(trainee.getUser().getId());
        traineeDao.delete(id);
        log.info(String.format("Trainee with the id: %d successfully deleted", id));
    }

    public void changePassword(Long traineeId, String password) throws InstanceNotFoundException {
        var trainee = traineeDao.get(traineeId);
        userDao.updatePassword(trainee.getUser().getId(), password);
        log.info(String.format("Password for the trainee with the id: %d successfully changed", traineeId));
    }

    public void changeIsActive(Long traineeId, Boolean isActive) throws InstanceNotFoundException {
        var trainee = traineeDao.get(traineeId);
        userDao.updateIsActive(trainee.getUser().getId(), isActive);
        log.info(String.format("Activity for the trainee with the id: %d successfully changed", traineeId));
    }

    public Trainee getByUsername(String username) throws InstanceNotFoundException {
        User user = userDao.getByUsername(username);

        List<Trainee> trainees = traineeDao.getAll().stream().filter(t -> t.getUser().equals(user)).toList();

        if (trainees.size() == 0) {
            throw new InstanceNotFoundException(String.format("Trainee not found with the username: %s", username));
        } else if (trainees.size() > 1) {
            throw new DuplicateKeyException(String.format("Multiple trainees found with the username : %s", username));
        } else return trainees.get(0);
    }

    public void deleteByUsername(String username) throws InstanceNotFoundException {
        Long id = userDao.getByUsername(username).getId();

        traineeDao.delete(id);
        log.info(String.format("Trainee with the username: %s successfully deleted", username));
    }

    public List<Training> getTrainingsByUsernameAndCriteria(String traineeUsername, LocalDate fromDate, LocalDate toDate,
                                                            String trainerName, TrainingType trainingType) throws InstanceNotFoundException {
        Trainee trainee = this.getByUsername(traineeUsername);

        Stream<Training> trainingStream = trainingDao.getAll().stream().filter(t -> t.getTrainee().equals(trainee));

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

    public List<Trainer> getTrainersUnassignedToTrainee(String username) throws InstanceNotFoundException {
        Trainee trainee = this.getByUsername(username);

        return trainerDao.getAll().stream().filter(t -> !trainee.getTrainers().contains(t)).toList();
    }

    public Set<Trainer> getTraineeTrainersList(Long id) throws InstanceNotFoundException {
        Trainee trainee = traineeDao.get(id);

        return trainee.getTrainers();
    }

    public void addTrainerToTraineesTrainersList(Long id, Trainer trainer) throws InstanceNotFoundException {
        Trainee trainee = traineeDao.get(id);

        trainee.getTrainers().add(trainer);
        trainer.getTrainees().add(trainee);
        log.info(String.format("Trainer with the id: %d has been added to the trainers list of trainee with the id: %d", trainer.getId(), id));
    }

    public void removeTrainerFromTraineesTrainersList(Long id, Trainer trainer) throws InstanceNotFoundException {
        Trainee trainee = traineeDao.get(id);

        trainee.getTrainers().remove(trainer);
        trainer.getTrainees().remove(trainee);
        log.info(String.format("Trainer with the id: %d has been removed from the trainers list of trainee with the id: %d", trainer.getId(), id));
    }

    public void changeUsername(Long traineeId, String username) throws InstanceNotFoundException {
        var trainee = traineeDao.get(traineeId);

        userDao.updateUsername(trainee.getUser().getId(), username);

        log.info(String.format("Username for the trainee with the id: %d successfully changed", traineeId));
    }

    public void changeFirstName(Long traineeId, String firstName) throws InstanceNotFoundException {
        var trainee = traineeDao.get(traineeId);

        userDao.updateFirstName(trainee.getUser().getId(), firstName);

        log.info(String.format("First name for the trainee with the id: %d successfully changed", traineeId));
    }

    public void changeLastName(Long traineeId, String lastName) throws InstanceNotFoundException {
        var trainee = traineeDao.get(traineeId);

        userDao.updateLastName(trainee.getUser().getId(), lastName);

        log.info(String.format("Last name for the trainee with the id: %d successfully changed", traineeId));
    }
}
