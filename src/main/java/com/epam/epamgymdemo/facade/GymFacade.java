package com.epam.epamgymdemo.facade;

import com.epam.epamgymdemo.model.Trainee;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.model.TrainingType;
import com.epam.epamgymdemo.model.User;
import com.epam.epamgymdemo.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.management.InstanceNotFoundException;
import javax.security.auth.login.CredentialNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final TrainingTypeService trainingTypeService;

    public String authenticate(String username, String password) throws InstanceNotFoundException, CredentialNotFoundException {
        String token = authenticationService.authenticateUser(username, password);

        if (token == null) {
            throw new CredentialNotFoundException("Invalid username or password");
        }

        return token;
    }

    public void changePassword(String username, String newPassword, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);

        User user = userService.getByUsername(username);

        userService.changePassword(user.getId(), newPassword);
    }
    public Trainee getTraineeById(Long id, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        return traineeService.getById(id);
    }

    public List<Trainee> getAllTrainees(String token) throws CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        return traineeService.getAll();
    }

    public Trainee getTraineeByUsername(String username, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        return traineeService.getByUsername(username);
    }

    public void createTrainee(LocalDate birthday, String address,
                                 String firstName, String lastName, Boolean isActive) throws InstanceNotFoundException {
        traineeService.create(birthday, address, firstName, lastName, isActive);
    }

    public void updateTrainee(Long traineeId, LocalDate doB, String address, Long userId, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        traineeService.update(traineeId, doB, address, userId);
    }

    public void deleteTraineeById(Long id, String token) throws CredentialNotFoundException, InstanceNotFoundException {
        authenticationService.isAuthorized(token);
        traineeService.deleteById(id);
    }

    public void deleteTraineeByUsername(String username, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        traineeService.deleteByUsername(username);
    }

    public void changeTraineesPassword(Long id, String password, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        traineeService.changePassword(id, password);
    }

    public void changeTraineesIsActive(Long id, Boolean isActive, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        traineeService.changeIsActive(id, isActive);
    }

    public List<Training> getTraineeTrainingsByUsernameAndCriteria(String traineeUsername, LocalDate fromDate, LocalDate toDate,
                                                                   String trainerName, TrainingType trainingType, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        return traineeService.getTrainingsByUsernameAndCriteria(traineeUsername, fromDate, toDate, trainerName, trainingType);
    }

    public List<Trainer> getTrainersUnassignedToTrainee(String username, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        return traineeService.getUnassignedTrainers(username);
    }

    public void addTrainerToTrainersList(Long id, Trainer trainer, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        traineeService.addTrainerToTrainersList(id, trainer);
    }

    public void removeTrainerFromTraineesTrainersList(Long id, Trainer trainer, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        traineeService.removeTrainerFromTrainersList(id, trainer);
    }

    public Trainer getTrainerById(Long id, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        return trainerService.getById(id);
    }

    public List<Trainer> getAllTrainers(String token) throws CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        return trainerService.getAll();
    }

    public Trainer getTrainerByUsername(String username, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        return trainerService.getByUsername(username);
    }

    public void createTrainer(Long typeId, String firstName, String lastName, Boolean isActive) throws InstanceNotFoundException {
        trainerService.create(typeId, firstName, lastName, isActive);
    }

    public void updateTrainer(Long trainerId, Long typeId, Long userId, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        trainerService.update(trainerId, typeId, userId);
    }

    public void changeTrainersPassword(Long id, String password, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        trainerService.changePassword(id, password);
    }

    public void changeTrainersIsActive(Long id, Boolean isActive, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        trainerService.changeIsActive(id, isActive);
    }

    public List<Training> getTrainerTrainingsByUsernameAndCriteria(String trainerUsername, LocalDate fromDate, LocalDate toDate,
                                                                   String traineeName, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        return trainerService.getTrainingsByUsernameAndCriteria(trainerUsername, fromDate, toDate, traineeName);
    }

    public Training getTrainingById(Long id, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        return trainingService.getById(id);
    }

    public List<Training> getAllTrainings(String token) throws CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        return trainingService.getAll();
    }

    public void createTraining(String trainingName, LocalDate trainingDate, Number trainingDuration,
                               Long traineeId, Long trainerId, Long typeId,
                               String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        trainingService.create(trainingName, trainingDate, trainingDuration, traineeId, trainerId, typeId);
    }

    public List<TrainingType> getAllTrainingTypes(String token) throws CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        return trainingTypeService.getAll();
    }
}