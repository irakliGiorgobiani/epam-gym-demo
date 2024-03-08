package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.model.Trainee;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.model.TrainingType;
import com.epam.epamgymdemo.repository.TrainingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingTypeService trainingTypeService;

    @Transactional
    public void create(String trainingName, LocalDate trainingDate, Number trainingDuration,
                       Long traineeId, Long trainerId, Long typeId) throws InstanceNotFoundException {
        Trainee trainee = traineeService.getById(traineeId);
        Trainer trainer = trainerService.getById(trainerId);
        TrainingType trainingType = trainingTypeService.getById(typeId);

        Training training = Training.builder()
                .trainingName(trainingName)
                .trainingDate(trainingDate)
                .trainingDuration(trainingDuration)
                .trainee(trainee)
                .trainer(trainer)
                .trainingType(trainingType)
                .build();
        trainingRepository.save(training);

        log.info(String.format("Training with the id: %d successfully created", training.getId()));

        trainee.getTrainers().add(trainer);
        trainer.getTrainees().add(trainee);
    }

    public Training getById(Long id) throws InstanceNotFoundException {
        return trainingRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException(String.format("Training not found with the id: %d", id)));
    }

    public List<Training> getAll() {
        return trainingRepository.findAll();
    }
}
