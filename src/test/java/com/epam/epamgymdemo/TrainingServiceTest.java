package com.epam.epamgymdemo;

import com.epam.epamgymdemo.dao.TraineeDao;
import com.epam.epamgymdemo.dao.TrainerDao;
import com.epam.epamgymdemo.dao.TrainingDao;
import com.epam.epamgymdemo.dao.TrainingTypeDao;
import com.epam.epamgymdemo.model.Trainee;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.model.TrainingType;
import com.epam.epamgymdemo.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TrainingServiceTest {

    @Mock
    private TrainingDao trainingDao;

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private TrainingTypeDao trainingTypeDao;

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
        trainingService = new TrainingService(trainingDao, traineeDao, trainerDao, trainingTypeDao);

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
        when(traineeDao.get(traineeId)).thenReturn(trainee);
        when(trainerDao.get(trainerId)).thenReturn(trainer);
        when(trainingTypeDao.get(typeId)).thenReturn(trainingType);

        trainingService.createTraining(trainingId, trainingName, trainingDate, trainingDuration, traineeId, trainerId, typeId);

        verify(trainingDao, times(1)).create(any(Training.class));
        assertEquals(1, trainee.getTrainers().size());
    }

    @Test
    void testSelectTraining() throws InstanceNotFoundException {
        when(trainingDao.get(trainingId)).thenReturn(training);

        Training selectedTraining = trainingService.selectTraining(trainingId);

        assertEquals(training, selectedTraining);
    }

    @Test
    void testSelectAllTrainings() {
        when(trainingDao.getAll()).thenReturn(List.of(training));

        List<Training> trainings = trainingService.selectAllTrainings();

        assertEquals(List.of(training), trainings);
    }
}

