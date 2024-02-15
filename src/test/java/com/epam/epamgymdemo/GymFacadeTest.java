package com.epam.epamgymdemo;

import com.epam.epamgymdemo.facade.GymFacade;
import com.epam.epamgymdemo.model.Trainee;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.model.TrainingType;
import com.epam.epamgymdemo.service.TraineeService;
import com.epam.epamgymdemo.service.TrainerService;
import com.epam.epamgymdemo.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GymFacadeTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private GymFacade gymFacade;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSelectTrainee() throws InstanceNotFoundException {
        Trainee trainee = new Trainee(1L, LocalDate.now(), "Address", 1L);
        when(traineeService.selectTrainee(1L)).thenReturn(trainee);

        Trainee selectedTrainee = gymFacade.selectTrainee(1L);

        assertEquals(trainee, selectedTrainee);
        verify(traineeService, times(1)).selectTrainee(1L);
    }

    @Test
    public void testSelectAllTrainees() {
        Trainee trainee = new Trainee(1L, LocalDate.now(), "Address", 1L);
        List<Trainee> trainees = Collections.singletonList(trainee);
        when(traineeService.selectAllTrainees()).thenReturn(trainees);

        List<Trainee> selectedTrainees = gymFacade.selectAllTrainees();

        assertEquals(trainees, selectedTrainees);
        verify(traineeService, times(1)).selectAllTrainees();
    }

    @Test
    public void testCreateTrainee() {
        LocalDate dob = LocalDate.now();
        gymFacade.createTrainee(1L, dob, "Address", 1L);
        verify(traineeService, times(1)).createTrainee(1L, dob, "Address", 1L);
    }

    @Test
    public void testUpdateTrainee() throws InstanceNotFoundException {
        LocalDate dob = LocalDate.now();
        gymFacade.updateTrainee(1L, dob, "Address", 1L);
        verify(traineeService, times(1)).updateTrainee(1L, dob, "Address", 1L);
    }

    @Test
    public void testDeleteTrainee() throws InstanceNotFoundException {
        gymFacade.deleteTrainee(1L);
        verify(traineeService, times(1)).deleteTrainee(1L);
    }

    @Test
    public void testSelectTrainer() throws InstanceNotFoundException {
        Trainer trainer = new Trainer(1L, TrainingType.YOGA, 1L);
        when(trainerService.selectTrainer(1L)).thenReturn(trainer);

        Trainer selectedTrainer = gymFacade.selectTrainer(1L);

        assertEquals(trainer, selectedTrainer);
        verify(trainerService, times(1)).selectTrainer(1L);
    }

    @Test
    public void testSelectAllTrainers() {
        Trainer trainer = new Trainer(1L, TrainingType.YOGA, 1L);
        List<Trainer> trainers = Collections.singletonList(trainer);
        when(trainerService.selectAllTrainers()).thenReturn(trainers);

        List<Trainer> selectedTrainers = gymFacade.selectAllTrainers();

        assertEquals(trainers, selectedTrainers);
        verify(trainerService, times(1)).selectAllTrainers();
    }

    @Test
    public void testCreateTrainer() {
        gymFacade.createTrainer(1L, TrainingType.YOGA, 1L);
        verify(trainerService, times(1)).createTrainer(1L, TrainingType.YOGA, 1L);
    }

    @Test
    public void testUpdateTrainer() throws InstanceNotFoundException {
        gymFacade.updateTrainer(1L, TrainingType.YOGA, 1L);
        verify(trainerService, times(1)).updateTrainer(1L, TrainingType.YOGA, 1L);
    }

    @Test
    public void testSelectTraining() throws InstanceNotFoundException {
        Training training = new Training(1L, 1L, 1L, "Training", TrainingType.YOGA, LocalDate.now(), 60);
        when(trainingService.selectTraining(1L)).thenReturn(training);

        Training selectedTraining = gymFacade.selectTraining(1L);

        assertEquals(training, selectedTraining);
    }

    @Test
    public void testSelectAllTrainings() {
        Training training = new Training(1L, 1L, 1L, "Training", TrainingType.YOGA, LocalDate.now(), 60);
        List<Training> trainings = Collections.singletonList(training);
        when(trainingService.selectAllTrainings()).thenReturn(trainings);

        List<Training> selectedTrainings = gymFacade.selectAllTrainings();

        assertEquals(trainings, selectedTrainings);
        verify(trainingService, times(1)).selectAllTrainings();
    }

    @Test
    public void testCreateTraining() {
        LocalDate trainingDate = LocalDate.now();
        gymFacade.createTraining(1L, 1L, 1L, "Training", TrainingType.YOGA, trainingDate, 60);
        verify(trainingService, times(1)).createTraining(1L, 1L, 1L, "Training", TrainingType.YOGA, trainingDate, 60);
    }

}

