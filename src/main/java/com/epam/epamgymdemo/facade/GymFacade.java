package com.epam.epamgymdemo.facade;

import com.epam.epamgymdemo.model.Trainee;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.model.TrainingType;
import com.epam.epamgymdemo.service.TraineeService;
import com.epam.epamgymdemo.service.TrainerService;
import com.epam.epamgymdemo.service.TrainingService;
import org.springframework.stereotype.Component;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Component
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public GymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public void authorize(String username, String password) throws InstanceNotFoundException {
        var trainer = trainerService.getByUsername(username);
        var trainee = traineeService.getByUsername(username);

        if ((trainee == null && trainer == null) ||
                (trainee != null && !trainee.getUser().getPassword().equals(password)) ||
                (trainer != null && !trainer.getUser().getPassword().equals(password))) {
            throw new InstanceNotFoundException("Invalid username or password");
        }
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

    public void createTrainee(Long traineeId, LocalDate doB, String address,
                              Long userId, String firstName, String lastName, Boolean isActive) throws InstanceNotFoundException {
        traineeService.createTrainee(traineeId, doB, address, userId, firstName, lastName, isActive);
    }

    public void updateTrainee(Long traineeId, LocalDate doB, String address, Long userId, String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        traineeService.updateTrainee(traineeId, doB, address, userId);
    }

    public void deleteTraineeById(Long id, String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        traineeService.deleteTrainee(id);
    }

    public void deleteTraineeByUsername(String username, String usernameAuth, String password) throws InstanceNotFoundException {
        authorize(usernameAuth, password);
        traineeService.deleteByUsername(username);
    }

    public void changeTraineesPassword(Long traineeId, String password, String username, String passwordAuth) throws InstanceNotFoundException {
        authorize(username, passwordAuth);
        traineeService.changePassword(traineeId, password);
    }

    public void changeTraineesIsActive(Long traineeId, Boolean isActive, String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        traineeService.changeIsActive(traineeId, isActive);
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

    public void createTrainer(Long trainerId, Long typeId,
                              Long userId, String firstName, String lastName, Boolean isActive) throws InstanceNotFoundException {
        trainerService.createTrainer(trainerId, typeId, userId, firstName, lastName, isActive);
    }

    public void updateTrainer(Long trainerId, Long typeId, Long userId, String username, String password) throws InstanceNotFoundException {
        authorize(username, password);
        trainerService.updateTrainer(trainerId, typeId, userId);
    }

    public void changeTrainersPassword(Long id, String password, String username, String passwordAuth) throws InstanceNotFoundException {
        authorize(username, passwordAuth);
        trainerService.changePassword(id, password);
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
}
