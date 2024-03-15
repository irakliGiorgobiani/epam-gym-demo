package com.epam.epamgymdemo.controller;

import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.model.dto.ActiveDto;
import com.epam.epamgymdemo.model.dto.TrainerDto;
import com.epam.epamgymdemo.model.dto.TrainerTrainingDto;
import com.epam.epamgymdemo.model.dto.TrainerWithListDto;
import com.epam.epamgymdemo.model.dto.UsernamePasswordDto;
import com.epam.epamgymdemo.service.AuthenticationService;
import com.epam.epamgymdemo.service.TrainerService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


class TrainerControllerTest {

    @InjectMocks
    private TrainerController trainerController;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserService userService;

    @Mock
    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        TrainerDto requestBody = new TrainerDto();
        UsernamePasswordDto usernamePasswordDto = new UsernamePasswordDto();

        when(userService.create(requestBody)).thenReturn(new User());
        when(userService.usernameAndPassword(any())).thenReturn(usernamePasswordDto);

        ResponseEntity<UsernamePasswordDto> responseEntity = trainerController.register(requestBody);

        assertEquals(usernamePasswordDto, responseEntity.getBody());
    }

    @Test
    void testGet() throws CredentialNotFoundException {
        String username = "testUser";
        String usernameAuth = "authUser";
        String password = "password";
        TrainerWithListDto trainerDto = new TrainerWithListDto();

        when(trainerService.get(username)).thenReturn(trainerDto);
        doNothing().when(authenticationService).authenticateUser(username, password);

        ResponseEntity<TrainerWithListDto> responseEntity = trainerController.get(username, usernameAuth, password);

        assertEquals(trainerDto, responseEntity.getBody());
    }

    @Test
    void testUpdate() throws CredentialNotFoundException {
        String username = "testUser";
        String usernameAuth = "authUser";
        String password = "password";
        TrainerDto requestBody = new TrainerDto();
        TrainerWithListDto updatedTrainerDto = new TrainerWithListDto();

        when(trainerService.update(username)).thenReturn(updatedTrainerDto);
        doNothing().when(authenticationService).authenticateUser(username, password);

        ResponseEntity<TrainerWithListDto> responseEntity = trainerController.update(username, usernameAuth, password, requestBody);

        assertEquals(updatedTrainerDto, responseEntity.getBody());
    }

    @Test
    void testGetTrainingList() throws CredentialNotFoundException {
        String username = "testUser";
        String usernameAuth = "authUser";
        String password = "password";
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = LocalDate.now().plusDays(1);
        String traineeName = "trainee";
        List<TrainerTrainingDto> trainings = List.of(new TrainerTrainingDto());

        when(trainerService.getTrainingsByUsernameAndCriteria(username, fromDate, toDate, traineeName)).thenReturn(trainings);
        doNothing().when(authenticationService).authenticateUser(username, password);

        ResponseEntity<List<TrainerTrainingDto>> responseEntity = trainerController.getTrainingList(username, fromDate, toDate, traineeName, usernameAuth, password);

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

        ResponseEntity<ActiveDto> responseEntity = trainerController.changeActive(username, isActive, usernameAuth, password);

        assertEquals(activeDto, responseEntity.getBody());
    }
}

