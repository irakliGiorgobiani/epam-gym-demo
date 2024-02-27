package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.dao.TraineeDao;
import com.epam.epamgymdemo.dao.TrainerDao;
import com.epam.epamgymdemo.dao.TrainingDao;
import com.epam.epamgymdemo.dao.TrainingTypeDao;
import com.epam.epamgymdemo.model.Trainee;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.model.TrainingType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class TrainingService {

    private final TrainingDao trainingDao;
    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;
    private final TrainingTypeDao trainingTypeDao;

    public TrainingService(TrainingDao trainingDao, TraineeDao traineeDao, TrainerDao trainerDao, TrainingTypeDao trainingTypeDao) {
        this.trainingDao = trainingDao;
        this.traineeDao = traineeDao;
        this.trainerDao = trainerDao;
        this.trainingTypeDao = trainingTypeDao;
    }

    public void createTraining(Long trainingId, String trainingName, LocalDate trainingDate, Number trainingDuration,
                               Long traineeId, Long trainerId, Long typeId) throws InstanceNotFoundException {
        Trainee trainee = traineeDao.get(traineeId);
        Trainer trainer = trainerDao.get(trainerId);
        TrainingType trainingType = trainingTypeDao.get(typeId);

        Training training = Training.builder()
                .id(trainingId)
                .trainingName(trainingName)
                .trainingDate(trainingDate)
                .trainingDuration(trainingDuration)
                .trainee(trainee)
                .trainer(trainer)
                .trainingType(trainingType)
                .build();
        trainingDao.create(training);

        log.info(String.format("Training with the id: %d successfully created", trainingId));

        trainee.getTrainers().add(trainer);
    }

    public Training selectTraining(Long id) throws InstanceNotFoundException {
        return trainingDao.get(id);
    }

    public List<Training> selectAllTrainings() {
        return trainingDao.getAll();
    }
}
