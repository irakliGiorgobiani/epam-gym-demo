package com.epam.epamgymdemo.controller;

import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.model.dto.ActiveDto;
import com.epam.epamgymdemo.model.dto.TraineeDto;
import com.epam.epamgymdemo.model.dto.TrainerDto;
import com.epam.epamgymdemo.model.dto.TraineeTrainingDto;
import com.epam.epamgymdemo.model.dto.UsernamePasswordDto;
import com.epam.epamgymdemo.service.AuthenticationService;
import com.epam.epamgymdemo.service.TraineeService;
import com.epam.epamgymdemo.service.TrainerService;
import com.epam.epamgymdemo.service.TrainingTypeService;
import com.epam.epamgymdemo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import javax.security.auth.login.CredentialNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


class TraineeControllerTest {

    @InjectMocks
    private TraineeController traineeController;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserService userService;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingTypeService trainingTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        TraineeDto requestBody = new TraineeDto();
        UsernamePasswordDto usernamePasswordDto = new UsernamePasswordDto();

        when(userService.create(requestBody)).thenReturn(new User());
        when(userService.usernameAndPassword(any())).thenReturn(usernamePasswordDto);

        ResponseEntity<UsernamePasswordDto> responseEntity = traineeController.register(requestBody);

        assertEquals(usernamePasswordDto, responseEntity.getBody());
    }

    @Test
    void testGet() throws CredentialNotFoundException {
        String username = "testUser";
        String usernameAuth = "authUser";
        String password = "password";
        TraineeDto traineeDto = new TraineeDto();

        when(traineeService.get(username)).thenReturn(traineeDto);
        doNothing().when(authenticationService).authenticateUser(username, password);

        ResponseEntity<TraineeDto> responseEntity = traineeController.get(username, usernameAuth, password);

        assertEquals(traineeDto, responseEntity.getBody());
    }

    @Test
    void testUpdate() throws CredentialNotFoundException {
        String username = "testUser";
        String usernameAuth = "authUser";
        String password = "password";
        TraineeDto requestBody = new TraineeDto();
        TraineeDto updatedTraineeDto = new TraineeDto();

        when(traineeService.update(username, requestBody)).thenReturn(updatedTraineeDto);
        doNothing().when(authenticationService).authenticateUser(username, password);

        ResponseEntity<TraineeDto> responseEntity = traineeController.update(username, usernameAuth, password, requestBody);

        assertEquals(updatedTraineeDto, responseEntity.getBody());
    }

    @Test
    void testDelete() throws CredentialNotFoundException {
        String username = "testUser";
        String usernameAuth = "authUser";
        String password = "password";
        TraineeDto traineeDto = new TraineeDto();

        when(traineeService.deleteByUsername(username)).thenReturn(traineeDto);
        doNothing().when(authenticationService).authenticateUser(username, password);

        ResponseEntity<TraineeDto> responseEntity = traineeController.delete(username, usernameAuth, password);

        assertEquals(traineeDto, responseEntity.getBody());
    }

    @Test
    void testGetNotAssignedTrainers() throws CredentialNotFoundException {
        String username = "testUser";
        String usernameAuth = "authUser";
        String password = "password";
        Set<TrainerDto> trainers = Set.of(new TrainerDto());

        when(traineeService.getUnassignedActiveTrainers(username)).thenReturn(trainers);
        doNothing().when(authenticationService).authenticateUser(username, password);

        ResponseEntity<Set<TrainerDto>> responseEntity = traineeController.getNotAssignedTrainers(username, usernameAuth, password);

        assertEquals(trainers, responseEntity.getBody());
    }

    @Test
    void testUpdateTrainerList() throws CredentialNotFoundException {
        String username = "testUser";
        String usernameAuth = "authUser";
        String password = "password";
        List<String> trainerUsernames = List.of("trainer1", "trainer2");
        Set<TrainerDto> trainers = Set.of(new TrainerDto());

        when(traineeService.addTrainerToTrainersList(username, trainerService.getByUsernames(trainerUsernames))).thenReturn(trainers);
        doNothing().when(authenticationService).authenticateUser(username, password);

        ResponseEntity<Set<TrainerDto>> responseEntity = traineeController.updateTrainerList(username, trainerUsernames, usernameAuth, password);

        assertEquals(trainers, responseEntity.getBody());
    }

    @Test
    void testGetTrainingList() throws CredentialNotFoundException {
        String username = "testUser";
        String usernameAuth = "authUser";
        String password = "password";
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = LocalDate.now().plusDays(1);
        String trainerName = "trainer";
        String typeName = "type";
        List<TraineeTrainingDto> trainings = List.of(new TraineeTrainingDto());

        when(traineeService.getTrainingsByUsernameAndCriteria(username, fromDate, toDate, trainerName, trainingTypeService.getTrainingTypeByName(typeName))).thenReturn(trainings);
        doNothing().when(authenticationService).authenticateUser(username, password);

        ResponseEntity<List<TraineeTrainingDto>> responseEntity = traineeController.getTrainingList(username, fromDate, toDate, trainerName, typeName, usernameAuth, password);

        assertEquals(trainings, responseEntity.getBody());
    }

    @Test
    void testChangeActive() throws CredentialNotFoundException {
        String username = "testUser";
        String usernameAuth = "authUser";
        String password = "password";
        Boolean isActive = true;
        ActiveDto activeDto = new ActiveDto();

        when(userService.changeActive(username, isActive)).thenReturn(activeDto);
        doNothing().when(authenticationService).authenticateUser(username, password);

        ResponseEntity<ActiveDto> responseEntity = traineeController.changeActive(username, isActive, usernameAuth, password);

        assertEquals(activeDto, responseEntity.getBody());
    }
}

