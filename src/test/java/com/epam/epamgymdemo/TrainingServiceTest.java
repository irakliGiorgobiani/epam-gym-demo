package com.epam.epamgymdemo;

import com.epam.epamgymdemo.model.Trainee;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.model.TrainingType;
import com.epam.epamgymdemo.repository.TrainingRepository;
import com.epam.epamgymdemo.service.TraineeService;
import com.epam.epamgymdemo.service.TrainerService;
import com.epam.epamgymdemo.service.TrainingService;
import com.epam.epamgymdemo.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TrainingServiceTest {
    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    @Mock
    private TrainingTypeService trainingTypeService;
    @InjectMocks
    private TrainingService trainingService;
    private Long trainingId;
    private String trainingName;
    private LocalDate trainingDate;
    private Number trainingDuration;
    private Long traineeId;
    private Long trainerId;
    private Long typeId;
    private Trainee trainee;
    private Trainer trainer;
    private TrainingType trainingType;
    private Training training;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainingService = new TrainingService(trainingRepository, trainerService, traineeService, trainingTypeService);

        trainingId = 1L;
        trainingName = "TrainingName";
        trainingDate = LocalDate.of(2024, 2, 1);
        trainingDuration = 1;
        traineeId = 1L;
        trainerId = 2L;
        typeId = 3L;

        trainee = Trainee.builder()
                .id(traineeId)
                .trainers(new HashSet<>())
                .build();

        trainer = Trainer.builder()
                .id(trainerId)
                .trainees(new HashSet<>())
                .build();

        trainingType = TrainingType.builder()
                .id(typeId)
                .build();

        training = Training.builder()
                .id(trainingId)
                .trainingName(trainingName)
                .trainingDate(trainingDate)
                .trainingDuration(trainingDuration)
                .trainee(trainee)
                .trainer(trainer)
                .trainingType(trainingType)
                .build();
    }

    @Test
    void testCreateTraining() throws InstanceNotFoundException {
        when(traineeService.getById(traineeId)).thenReturn(trainee);
        when(trainerService.getById(trainerId)).thenReturn(trainer);
        when(trainingTypeService.getById(typeId)).thenReturn(trainingType);

        trainingService.create(trainingName, trainingDate, trainingDuration, traineeId, trainerId, typeId);

        verify(trainingRepository, times(1)).save(any(Training.class));
        assertEquals(1, trainee.getTrainers().size());
    }

    @Test
    void testSelectTraining() throws InstanceNotFoundException {
        when(trainingRepository.findById(trainingId)).thenReturn(Optional.ofNullable(training));

        Training selectedTraining = trainingService.getById(trainingId);

        assertEquals(training, selectedTraining);
    }

    @Test
    void testSelectAllTrainings() {
        when(trainingRepository.findAll()).thenReturn(List.of(training));

        List<Training> trainings = trainingService.getAll();

        assertEquals(List.of(training), trainings);
    }
}