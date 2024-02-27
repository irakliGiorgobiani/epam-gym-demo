package com.epam.epamgymdemo.facade;

import com.epam.epamgymdemo.model.Trainee;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.Training;
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

    public Trainee selectTrainee(Long id) throws InstanceNotFoundException {
        return traineeService.selectTrainee(id);
    }

    public List<Trainee> selectAllTrainees() {
        return traineeService.selectAllTrainees();
    }

    public void createTrainee(Long traineeId, LocalDate doB, String address,
                              Long userId, String firstName, String lastName, Boolean isActive) throws InstanceNotFoundException {
        traineeService.createTrainee(traineeId, doB, address, userId, firstName, lastName, isActive);
    }

    public void updateTrainee(Long traineeId, LocalDate doB, String address, Long userId) throws InstanceNotFoundException {
        traineeService.updateTrainee(traineeId, doB, address, userId);
    }

    public void deleteTrainee(Long id) throws InstanceNotFoundException {
        traineeService.deleteTrainee(id);
    }

    public Trainer selectTrainer(Long id) throws InstanceNotFoundException {
        return trainerService.selectTrainer(id);
    }

    public List<Trainer> selectAllTrainers() {
        return trainerService.selectAllTrainers();
    }

    public void createTrainer(Long trainerId, Long typeId,
                              Long userId, String firstName, String lastName, Boolean isActive) throws InstanceNotFoundException {
        trainerService.createTrainer(trainerId, typeId, userId, firstName, lastName, isActive);
    }

    public void updateTrainer(Long trainerId, Long typeId, Long userId) throws InstanceNotFoundException {
        trainerService.updateTrainer(trainerId, typeId, userId);
    }

    public Training selectTraining(Long id) throws InstanceNotFoundException {
        return trainingService.selectTraining(id);
    }

    public List<Training> selectAllTrainings() {
        return trainingService.selectAllTrainings();
    }

    public void createTraining(Long trainingId, String trainingName, LocalDate trainingDate, Number trainingDuration,
                               Long traineeId, Long trainerId, Long typeId) throws InstanceNotFoundException {
        trainingService.createTraining(trainingId, trainingName, trainingDate, trainingDuration, traineeId, trainerId, typeId);
    }
}
