package com.epam.epamgymdemo;

import com.epam.epamgymdemo.facade.GymFacade;
import com.epam.epamgymdemo.model.Trainee;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.User;
import com.epam.epamgymdemo.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceNotFoundException;
import javax.security.auth.login.CredentialNotFoundException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GymFacadeTest {
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    @Mock
    private TrainingService trainingService;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private UserService userService;
    @Mock
    private TrainingTypeService trainingTypeService;
    @InjectMocks
    private GymFacade gymFacade;
    private Trainer trainer;
    private Trainee trainee;
    private String token;

    @BeforeEach
    void setUp() throws InstanceNotFoundException {
        gymFacade = new GymFacade(traineeService, trainerService, trainingService, authenticationService, userService, trainingTypeService);

        trainer = Trainer.builder()
                .id(1L)
                .user(User.builder()
                        .username("trainer")
                        .password("password")
                        .build())
                .build();

        trainee = Trainee.builder()
                .id(1L)
                .user(User.builder()
                        .username("trainee")
                        .password("password")
                        .build())
                .birthday(LocalDate.now())
                .address("123 Street")
                .build();

        token = authenticationService.authenticateUser("trainee", "password");

        Mockito.lenient().when(trainerService.getByUsername("trainer")).thenReturn(new Trainer());
        Mockito.lenient().when(trainerService.getByUsername("trainee")).thenReturn(new Trainer());
    }

    @Test
    void testAuthenticate() throws InstanceNotFoundException, CredentialNotFoundException {
        when(authenticationService.authenticateUser("username", "password")).thenReturn("token");

        String token = gymFacade.authenticate("username", "password");

        assertNotNull(token);
    }

    @Test
    void testSelectTrainee() throws InstanceNotFoundException, CredentialNotFoundException {
        when(traineeService.getById(1L)).thenReturn(trainee);

        Trainee selectedTrainee = gymFacade.getTraineeById(1L, token);

        assertEquals(trainee, selectedTrainee);

        verify(traineeService, times(1)).getById(1L);
    }

    @Test
    void testSelectAllTrainees() throws CredentialNotFoundException {
        List<Trainee> trainees = Collections.singletonList(trainee);
        when(traineeService.getAll()).thenReturn(trainees);

        List<Trainee> selectedTrainees = gymFacade.getAllTrainees("token");

        assertEquals(trainees, selectedTrainees);

        verify(traineeService, times(1)).getAll();
    }

    @Test
    void testCreateTrainee() throws InstanceNotFoundException {
        gymFacade.createTrainee(LocalDate.now(), "123 Street", "John", "Doe", true);

        verify(traineeService, times(1)).create( LocalDate.now(), "123 Street", "John", "Doe", true);
    }

    @Test
    void testSelectTrainer() throws InstanceNotFoundException, CredentialNotFoundException {
        when(trainerService.getById(1L)).thenReturn(trainer);

        Trainer selectedTrainer = gymFacade.getTrainerById(1L, token);

        assertEquals(trainer, selectedTrainer);

        verify(trainerService, times(1)).getById(1L);
    }

    @Test
    void testSelectAllTrainers() throws CredentialNotFoundException {
        List<Trainer> trainers = Collections.singletonList(trainer);
        when(trainerService.getAll()).thenReturn(trainers);

        List<Trainer> selectedTrainers = gymFacade.getAllTrainers(token);

        assertEquals(trainers, selectedTrainers);

        verify(trainerService, times(1)).getAll();
    }

    @Test
    void testCreateTrainer() throws InstanceNotFoundException {
        gymFacade.createTrainer(1L, "John", "Doe", true);

        verify(trainerService, times(1)).create(1L, "John", "Doe", true);
    }

    @Test
    void testUpdateTrainee() throws InstanceNotFoundException, CredentialNotFoundException {
        gymFacade.updateTrainee(1L, LocalDate.now(), "Address", 1L, token);

        verify(traineeService, times(1)).update(1L, LocalDate.now(), "Address", 1L);
    }

    @Test
    void testDeleteTraineeById() throws CredentialNotFoundException, InstanceNotFoundException {
        gymFacade.deleteTraineeById(1L, token);

        verify(traineeService, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTraineeByUsername() throws InstanceNotFoundException, CredentialNotFoundException {
        gymFacade.deleteTraineeByUsername("trainee", token);

        verify(traineeService, times(1)).deleteByUsername("trainee");
    }

    @Test
    void testChangeTraineesPassword() throws InstanceNotFoundException, CredentialNotFoundException {
        gymFacade.changeTraineesPassword(1L, "newpassword", token);

        verify(traineeService, times(1)).changePassword(1L, "newpassword");
    }

    @Test
    public void testChangeTraineesIsActive() throws InstanceNotFoundException, CredentialNotFoundException {
        Long traineeId = 1L;
        Boolean isActive = true;

        gymFacade.changeTraineesIsActive(traineeId, isActive, token);

        verify(traineeService, times(1)).changeIsActive(traineeId, isActive);
    }
}
