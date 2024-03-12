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
import javax.naming.NamingException;
import javax.security.auth.login.CredentialNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class GymFacade {

    private final TraineeService traineeService;

    private final TrainerService trainerService;

    private final TrainingService trainingService;

    private final AuthenticationService authenticationService;

    private final UserService userService;

    private final TrainingTypeService trainingTypeService;

    public String authenticate(String username, String password) throws CredentialNotFoundException {
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

    public Trainee createTrainee(LocalDate birthday, String address,
                                 String firstName, String lastName, Boolean isActive) throws InstanceNotFoundException, NamingException {
        User user = userService.create(firstName, lastName, isActive);

        return traineeService.create(birthday, address, user);
    }

    public void updateTrainee(Long traineeId, LocalDate doB, String address, Long userId, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        traineeService.update(traineeId, doB, address, userId);
    }

    public Trainee getTraineeById(Long id, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        return traineeService.getById(id);
    }

    public Trainee getTraineeByUsername(String username, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        return traineeService.getByUsername(username);
    }

    public List<Trainee> getAllTrainees(String token) throws CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        return traineeService.getAll();
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

    public Set<Trainer> getTraineesTrainerList(Long id, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        return traineeService.getTraineeTrainersList(id);
    }

    public void addTrainerToTrainersList(Long id, Trainer trainer, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        traineeService.addTrainerToTrainersList(id, trainer);
    }

    public void deleteTraineeById(Long id, String token) throws CredentialNotFoundException, InstanceNotFoundException {
        authenticationService.isAuthorized(token);
        traineeService.deleteById(id);
    }

    public void deleteTraineeByUsername(String username, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        traineeService.deleteByUsername(username);
    }

    public void deleteTrainerFromTraineesTrainersList(Long id, Trainer trainer, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        traineeService.deleteTrainerFromTrainersList(id, trainer);
    }

    public void changeTraineesUsername(Long id, String newUsername, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        Long traineeId = traineeService.getUserId(id);
        userService.changeUsername(traineeId, newUsername);
    }

    public void changeTraineesPassword(Long id, String password, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        Long traineeId = traineeService.getUserId(id);
        userService.changePassword(traineeId, password);
    }

    public void changeTraineesFirstName(Long id, String firstName, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        Long traineeId = traineeService.getUserId(id);
        userService.changeFirstName(traineeId, firstName);
    }

    public void changeTraineesLastName(Long id, String lastName, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        Long traineeId = traineeService.getUserId(id);
        userService.changeLastName(traineeId, lastName);
    }

    public void changeTraineesIsActive(Long id, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        Long traineeId = traineeService.getUserId(id);
        userService.changeIsActive(traineeId);
    }

    public Trainer createTrainer(Long typeId, String firstName, String lastName, Boolean isActive) throws InstanceNotFoundException, NamingException {
        User user = userService.create(firstName, lastName, isActive);
        return trainerService.create(typeId, user);
    }

    public void updateTrainer(Long trainerId, Long typeId, Long userId, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        trainerService.update(trainerId, typeId, userId);
    }

    public Trainer getTrainerById(Long id, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        return trainerService.getById(id);
    }

    public Trainer getTrainerByUsername(String username, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        return trainerService.getByUsername(username);
    }

    public List<Trainer> getAllTrainers(String token) throws CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        return trainerService.getAll();
    }

    public List<Training> getTrainerTrainingsByUsernameAndCriteria(String trainerUsername, LocalDate fromDate, LocalDate toDate,
                                                                   String traineeName, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        return trainerService.getTrainingsByUsernameAndCriteria(trainerUsername, fromDate, toDate, traineeName);
    }

    public void changeTrainersUsername(Long id, String newUsername, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        Long trainerId = trainerService.getUserId(id);
        userService.changeUsername(trainerId, newUsername);
    }

    public void changeTrainersPassword(Long id, String password, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        Long trainerId = trainerService.getUserId(id);
        userService.changePassword(trainerId, password);
    }

    public void changeTrainersFirstName(Long id, String firstName, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        Long trainerId = trainerService.getUserId(id);
        userService.changeFirstName(trainerId, firstName);
    }

    public void changeTrainersLastName(Long id, String lastName, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        Long trainerId = trainerService.getUserId(id);
        userService.changeLastName(trainerId, lastName);
    }

    public void changeTrainersIsActive(Long id, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        Long trainerId = trainerService.getUserId(id);
        userService.changeIsActive(trainerId);
    }

    public Training createTraining(String trainingName, LocalDate trainingDate, Number trainingDuration,
                               Long traineeId, Long trainerId, Long typeId,
                               String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        Trainee trainee = traineeService.getById(traineeId);
        Trainer trainer = trainerService.getById(trainerId);
        TrainingType trainingType = trainingTypeService.getById(typeId);
        return trainingService.create(trainingName, trainingDate, trainingDuration, trainee, trainer, trainingType);
    }

    public Training getTrainingById(Long id, String token) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        return trainingService.getById(id);
    }

    public List<Training> getAllTrainings(String token) throws CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        return trainingService.getAll();
    }

    public TrainingType getTrainingTypeByName(String trainingType, String token) throws CredentialNotFoundException, InstanceNotFoundException {
        authenticationService.isAuthorized(token);
        return trainingTypeService.getTrainingTypeByName(trainingType);
    }

    public List<TrainingType> getAllTrainingTypes(String token) throws CredentialNotFoundException {
        authenticationService.isAuthorized(token);
        return trainingTypeService.getAll();
    }
}
