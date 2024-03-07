package com.epam.epamgymdemo.facade;

import com.epam.epamgymdemo.model.Trainee;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.model.TrainingType;
import com.epam.epamgymdemo.service.TraineeService;
import com.epam.epamgymdemo.service.TrainerService;
import com.epam.epamgymdemo.service.TrainingService;
import com.epam.epamgymdemo.service.UserService;
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
    private final UserService userService;

    public void authorize(String username, String password) throws InstanceNotFoundException, CredentialNotFoundException {
        var user = userService.getByUsername(username);

        if (user == null || !user.getPassword().equals(password)) {
            throw new CredentialNotFoundException("Invalid username or password");
        }
    }

    public Trainee getTraineeById(Long id, String username, String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authorize(username, password);
        return traineeService.getById(id);
    }

    public List<Trainee> getAllTrainees(String username, String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authorize(username, password);
        return traineeService.getAll();
    }

    public Trainee getTraineeByUsername(String username, String usernameAuth, String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authorize(usernameAuth, password);
        return traineeService.getByUsername(username);
    }

    public void createTrainee(LocalDate birthday, String address,
                              String firstName, String lastName, Boolean isActive) throws InstanceNotFoundException {
        traineeService.create(birthday, address, firstName, lastName, isActive);
    }

    public void updateTrainee(Long traineeId, LocalDate doB, String address, Long userId, String username, String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authorize(username, password);
        traineeService.update(traineeId, doB, address, userId);
    }

    public void deleteTraineeById(Long id, String username, String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authorize(username, password);
        traineeService.deleteById(id);
    }

    public void deleteTraineeByUsername(String username, String usernameAuth, String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authorize(usernameAuth, password);
        traineeService.deleteByUsername(username);
    }

    public void changeTraineesPassword(Long traineeId, String password, String username, String passwordAuth) throws InstanceNotFoundException, CredentialNotFoundException {
        authorize(username, passwordAuth);
        traineeService.changePassword(traineeId, password);
    }

    public void changeTraineesIsActive(Long traineeId, Boolean isActive, String username, String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authorize(username, password);
        traineeService.changeIsActive(traineeId, isActive);
    }

    public List<Training> getTraineeTrainingsByUsernameAndCriteria(String traineeUsername, LocalDate fromDate, LocalDate toDate,
                                                                   String trainerName, TrainingType trainingType, String username, String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authorize(username, password);
        return traineeService.getTrainingsByUsernameAndCriteria(traineeUsername, fromDate, toDate, trainerName, trainingType);
    }

    public List<Trainer> getTrainersUnassignedToTrainee(String username, String usernameAuth, String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authorize(usernameAuth, password);
        return traineeService.getUnassignedTrainers(username);
    }

    public void addTrainerToTrainersList(Long id, Trainer trainer, String username, String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authorize(username, password);
        traineeService.addTrainerToTrainersList(id, trainer);
    }

    public void removeTrainerFromTraineesTrainersList(Long id, Trainer trainer, String username, String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authorize(username, password);
        traineeService.removeTrainerFromTrainersList(id, trainer);
    }

    public Trainer getTrainerById(Long id, String username, String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authorize(username, password);
        return trainerService.getById(id);
    }

    public List<Trainer> getAllTrainers(String username, String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authorize(username, password);
        return trainerService.selectAllTrainers();
    }

    public Trainer getTrainerByUsername(String username, String usernameAuth, String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authorize(usernameAuth, password);
        return trainerService.getByUsername(username);
    }

    public void createTrainer(Long typeId, String firstName, String lastName, Boolean isActive) throws InstanceNotFoundException {
        trainerService.create(typeId, firstName, lastName, isActive);
    }

    public void updateTrainer(Long trainerId, Long typeId, Long userId, String username, String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authorize(username, password);
        trainerService.update(trainerId, typeId, userId);
    }

    public void changeTrainersPassword(Long id, String password, String username, String passwordAuth) throws InstanceNotFoundException, CredentialNotFoundException {
        authorize(username, passwordAuth);
        trainerService.changePassword(id, password);
    }

    public void changeTrainersIsActive(Long id, Boolean isActive, String username, String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authorize(username, password);
        trainerService.changeIsActive(id, isActive);
    }

    public List<Training> selectTrainerTrainingsByUsernameAndCriteria(String trainerUsername, LocalDate fromDate, LocalDate toDate,
                                                            String traineeName, String username, String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authorize(username, password);
        return trainerService.getTrainingsByUsernameAndCriteria(trainerUsername, fromDate, toDate, traineeName);
    }

    public Training getTrainingById(Long id, String username, String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authorize(username, password);
        return trainingService.getById(id);
    }

    public List<Training> getAllTrainings(String username, String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authorize(username, password);
        return trainingService.getAll();
    }

    public void createTraining(String trainingName, LocalDate trainingDate, Number trainingDuration,
                               Long traineeId, Long trainerId, Long typeId,
                               String username, String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authorize(username, password);
        trainingService.create(trainingName, trainingDate, trainingDuration, traineeId, trainerId, typeId);
    }
}
