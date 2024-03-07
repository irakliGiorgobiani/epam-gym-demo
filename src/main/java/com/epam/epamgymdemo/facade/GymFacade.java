package com.epam.epamgymdemo.facade;

import com.epam.epamgymdemo.model.*;
import com.epam.epamgymdemo.service.TraineeService;
import com.epam.epamgymdemo.service.TrainerService;
import com.epam.epamgymdemo.service.TrainingService;
import com.epam.epamgymdemo.service.UserService;
import org.springframework.stereotype.Component;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final UserService userService;

    public GymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService, UserService userService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        this.userService = userService;
    }

    public void authorize(String username, String password) throws InstanceNotFoundException {
        var user = userService.getByUsername(username);

        if (user == null || !user.getPassword().equals(password)) {
            throw new InstanceNotFoundException("Invalid username or password");
        }
    }

    public void changePassword(String username, String oldPassword, String newPassword) throws InstanceNotFoundException {
        authorize(username, oldPassword);

        User user = userService.getByUsername(username);

        userService.updatePassword(user.getId(), newPassword);
    }


    public Trainee selectTrainee(Long id, String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        return traineeService.selectTrainee(id);
    }

    public List<Trainee> selectAllTrainees(String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        return traineeService.selectAllTrainees();
    }

    public Trainee selectTraineeByUsername(String username, String usernameAuth, String password) throws InstanceNotFoundException {
        authorize(usernameAuth, password);
        return traineeService.getByUsername(username);
    }

    public Trainee createTrainee(LocalDate dateOfBirth, String address,
                              String firstName, String lastName, Boolean isActive) {
        return traineeService.createTrainee(dateOfBirth, address, firstName, lastName, isActive);
    }

    public void updateTrainee(Long traineeId, LocalDate dateOfBirth, String address, Long userId, String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        traineeService.updateTrainee(traineeId, dateOfBirth, address, userId);
    }

    public void deleteTraineeById(Long id, String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        traineeService.deleteTrainee(id);
    }

    public void deleteTraineeByUsername(String username, String usernameAuth, String password) throws InstanceNotFoundException {
        authorize(usernameAuth, password);
        traineeService.deleteByUsername(username);
    }

    public void changeTraineesPassword(Long id, String password, String username, String passwordAuth) throws InstanceNotFoundException {
        authorize(username, passwordAuth);
        traineeService.changePassword(id, password);
    }

    public void changeTraineesUsername(Long id, String newUsername, String oldUsername, String password) throws InstanceNotFoundException {
        authorize(oldUsername, password);
        traineeService.changeUsername(id, newUsername);
    }

    public void changeTraineesFirstName(Long id, String firstName, String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        traineeService.changeFirstName(id, firstName);
    }

    public void changeTraineesLastName(Long id, String lastName, String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        traineeService.changeLastName(id, lastName);
    }

    public void changeTraineesIsActive(Long id, Boolean isActive, String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        traineeService.changeIsActive(id, isActive);
    }

    public List<Training> selectTraineeTrainingsByUsernameAndCriteria(String traineeUsername, LocalDate fromDate, LocalDate toDate,
                                                                   String trainerName, TrainingType trainingType, String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        return traineeService.getTrainingsByUsernameAndCriteria(traineeUsername, fromDate, toDate, trainerName, trainingType);
    }

    public List<Trainer> selectTrainersUnassignedToTrainee(String username, String usernameAuth, String password) throws InstanceNotFoundException {
        authorize(usernameAuth, password);
        return traineeService.getTrainersUnassignedToTrainee(username);
    }

    public Set<Trainer> selectTraineesTrainerList(Long id, String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        return traineeService.getTraineeTrainersList(id);
    }

    public void addTrainerToTrainersList(Long id, Trainer trainer, String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        traineeService.addTrainerToTraineesTrainersList(id, trainer);
    }

    public void removeTrainerFromTraineesTrainersList(Long id, Trainer trainer, String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        traineeService.removeTrainerFromTraineesTrainersList(id, trainer);
    }

    public Trainer selectTrainer(Long id, String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        return trainerService.selectTrainer(id);
    }

    public List<Trainer> selectAllTrainers(String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        return trainerService.selectAllTrainers();
    }

    public Trainer selectTrainerByUsername(String username, String usernameAuth, String password) throws InstanceNotFoundException {
        authorize(usernameAuth, password);
        return trainerService.getByUsername(username);
    }

    public Trainer createTrainer(Long typeId, String firstName, String lastName, Boolean isActive) throws InstanceNotFoundException {
        return trainerService.createTrainer(typeId, firstName, lastName, isActive);
    }

    public void updateTrainer(Long trainerId, Long typeId, Long userId, String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        trainerService.updateTrainer(trainerId, typeId, userId);
    }

    public void changeTrainersPassword(Long id, String password, String username, String passwordAuth) throws InstanceNotFoundException {
        authorize(username, passwordAuth);
        trainerService.changePassword(id, password);
    }

    public void changeTrainersUsername(Long id, String newUsername, String oldUsername, String password) throws InstanceNotFoundException {
        authorize(oldUsername, password);
        trainerService.changeUsername(id, newUsername);
    }

    public void changeTrainersFirstName(Long id, String firstName, String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        trainerService.changeFirstName(id, firstName);
    }

    public void changeTrainersLastName(Long id, String lastName, String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        trainerService.changeLastName(id, lastName);
    }

    public void changeTrainersIsActive(Long id, Boolean isActive, String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        trainerService.changeIsActive(id, isActive);
    }

    public List<Training> selectTrainerTrainingsByUsernameAndCriteria(String trainerUsername, LocalDate fromDate, LocalDate toDate,
                                                            String traineeName, String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        return trainerService.getTrainingsByUsernameAndCriteria(trainerUsername, fromDate, toDate, traineeName);
    }

    public Training selectTraining(Long id, String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        return trainingService.selectTraining(id);
    }

    public List<Training> selectAllTrainings(String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        return trainingService.selectAllTrainings();
    }

    public void createTraining(Long trainingId, String trainingName, LocalDate trainingDate, Number trainingDuration,
                               Long traineeId, Long trainerId, Long typeId) throws InstanceNotFoundException {
        trainingService.createTraining(trainingId, trainingName, trainingDate, trainingDuration, traineeId, trainerId, typeId);
    }

    public TrainingType getTrainingTypeByName(String trainingType, String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        return trainingService.getTrainingTypeByName(trainingType);
    }
}
