package com.epam.epamgymdemo;

import com.epam.epamgymdemo.facade.GymFacade;
import com.epam.epamgymdemo.model.Trainee;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.User;
import com.epam.epamgymdemo.service.TraineeService;
import com.epam.epamgymdemo.service.TrainerService;
import com.epam.epamgymdemo.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GymFacadeTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private GymFacade gymFacade;

    private Trainer trainer;
    private Trainee trainee;

    @BeforeEach
    void setUp() throws InstanceNotFoundException {
        gymFacade = new GymFacade(traineeService, trainerService, trainingService);

        trainer = Trainer.builder()
                .id(1L)
                .user(User.builder()
                        .userName("trainer")
                        .password("password")
                        .build())
                .build();

        trainee = Trainee.builder()
                .id(1L)
                .user(User.builder()
                        .userName("trainee")
                        .password("password")
                        .build())
                .dateOfBirth(LocalDate.now())
                .address("123 Street")
                .build();

        Mockito.lenient().when(trainerService.getByUsername("trainer")).thenReturn(new Trainer());
        Mockito.lenient().when(trainerService.getByUsername("trainee")).thenReturn(new Trainer());
    }

    @Test
    void testSelectTrainee() throws InstanceNotFoundException {
        when(trainerService.getByUsername("trainer")).thenReturn(trainer);
        when(traineeService.selectTrainee(1L)).thenReturn(trainee);

        Trainee selectedTrainee = gymFacade.selectTrainee(1L, "trainer", "password");

        assertEquals(trainee, selectedTrainee);

        verify(trainerService, times(1)).getByUsername("trainer");
        verify(traineeService, times(1)).selectTrainee(1L);
    }

    @Test
    void testSelectAllTrainees() throws InstanceNotFoundException {
        when(trainerService.getByUsername("trainer")).thenReturn(trainer);
        List<Trainee> trainees = Collections.singletonList(trainee);
        when(traineeService.selectAllTrainees()).thenReturn(trainees);

        List<Trainee> selectedTrainees = gymFacade.selectAllTrainees("trainer", "password");

        assertEquals(trainees, selectedTrainees);

        verify(trainerService, times(1)).getByUsername("trainer");
        verify(traineeService, times(1)).selectAllTrainees();
    }

    @Test
    void testCreateTrainee() throws InstanceNotFoundException {
        gymFacade.createTrainee(1L, LocalDate.now(), "123 Street", 1L, "John", "Doe", true);

        verify(traineeService, times(1)).createTrainee(1L, LocalDate.now(), "123 Street", 1L, "John", "Doe", true);
    }

    @Test
    void testSelectTrainer() throws InstanceNotFoundException {
        when(trainerService.getByUsername("trainer")).thenReturn(trainer);
        when(trainerService.selectTrainer(1L)).thenReturn(trainer);

        Trainer selectedTrainer = gymFacade.selectTrainer(1L, "trainer", "password");

        assertEquals(trainer, selectedTrainer);

        verify(trainerService, times(1)).selectTrainer(1L);
    }

    @Test
    void testSelectAllTrainers() throws InstanceNotFoundException {
        when(trainerService.getByUsername("trainer")).thenReturn(trainer);
        List<Trainer> trainers = Collections.singletonList(trainer);
        when(trainerService.selectAllTrainers()).thenReturn(trainers);

        List<Trainer> selectedTrainers = gymFacade.selectAllTrainers("trainer", "password");

        assertEquals(trainers, selectedTrainers);

        verify(trainerService, times(1)).selectAllTrainers();
    }

    @Test
    void testCreateTrainer() throws InstanceNotFoundException {
        gymFacade.createTrainer(1L, 1L, 1L, "John", "Doe", true);

        verify(trainerService, times(1)).createTrainer(1L, 1L, 1L, "John", "Doe", true);
    }

    @Test
    void testUpdateTrainee() throws InstanceNotFoundException {
        when(trainerService.getByUsername("trainer")).thenReturn(trainer);
        gymFacade.updateTrainee(1L, LocalDate.now(), "Address", 1L, "trainer", "password");

        verify(traineeService, times(1)).updateTrainee(1L, LocalDate.now(), "Address", 1L);
    }

    @Test
    void testDeleteTraineeById() throws InstanceNotFoundException {
        when(trainerService.getByUsername("trainer")).thenReturn(trainer);
        gymFacade.deleteTraineeById(1L, "trainer", "password");

        verify(traineeService, times(1)).deleteTrainee(1L);
    }

    @Test
    void testDeleteTraineeByUsername() throws InstanceNotFoundException {
        when(trainerService.getByUsername("trainer")).thenReturn(trainer);
        gymFacade.deleteTraineeByUsername("trainee", "trainer", "password");

        verify(traineeService, times(1)).deleteByUsername("trainee");
    }

    @Test
    void testChangeTraineesPassword() throws InstanceNotFoundException {
        when(trainerService.getByUsername("trainer")).thenReturn(trainer);
        gymFacade.changeTraineesPassword(1L, "newpassword", "trainer", "password");

        verify(traineeService, times(1)).changePassword(1L, "newpassword");
    }

    @Test
    public void testChangeTraineesIsActive() throws InstanceNotFoundException {
        when(trainerService.getByUsername("trainer")).thenReturn(trainer);
        Long traineeId = 1L;
        Boolean isActive = true;

        gymFacade.changeTraineesIsActive(traineeId, isActive, "trainer", "password");

        verify(traineeService, times(1)).changeIsActive(traineeId, isActive);
    }
}
