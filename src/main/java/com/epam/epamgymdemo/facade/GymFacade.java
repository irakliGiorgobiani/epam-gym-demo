package com.epam.epamgymdemo.facade;

import com.epam.epamgymdemo.Dao.TraineeDao;
import com.epam.epamgymdemo.Dao.TrainerDao;
import com.epam.epamgymdemo.Dao.TrainingDao;
import com.epam.epamgymdemo.model.Trainee;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.model.TrainingType;
import com.epam.epamgymdemo.repository.TraineeRepository;
import com.epam.epamgymdemo.repository.TrainerRepository;
import com.epam.epamgymdemo.repository.TrainingRepository;
import com.epam.epamgymdemo.service.TraineeService;
import com.epam.epamgymdemo.service.TrainerService;
import com.epam.epamgymdemo.service.TrainingService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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

    public void createTrainee(Long traineeId, LocalDate doB, String address, Long userId) {
        traineeService.createTrainee(traineeId, doB, address, userId);
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

    public void createTrainer(Long trainerId, TrainingType specialization, Long userId) {
        trainerService.createTrainer(trainerId, specialization, userId);
    }

    public void updateTrainer(Long trainerId, TrainingType specialization, Long userId) throws InstanceNotFoundException {
        trainerService.updateTrainer(trainerId, specialization, userId);
    }

    public Training selectTraining(Long id) throws InstanceNotFoundException {
        return trainingService.selectTraining(id);
    }

    public List<Training> selectAllTrainings() {
        return trainingService.selectAllTrainings();
    }

    public void createTraining(Long trainingId, Long traineeId, Long trainerId, String trainingName,
                               TrainingType trainingType, LocalDate trainingDate, Integer trainingDuration) {
        trainingService.createTraining(trainingId, traineeId, trainerId, trainingName,
                trainingType, trainingDate, trainingDuration);
    }
}
