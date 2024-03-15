package com.epam.epamgymdemo.controller;

import com.epam.epamgymdemo.model.dto.TrainingDto;
import com.epam.epamgymdemo.service.AuthenticationService;
import com.epam.epamgymdemo.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import javax.security.auth.login.CredentialNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;


class TrainingControllerTest {

    @InjectMocks
    private TrainingController trainingController;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddTraining() throws CredentialNotFoundException {
        TrainingDto requestBody = new TrainingDto();
        String username = "testUser";
        String password = "password";

        doNothing().when(authenticationService).authenticateUser(username, password);

        ResponseEntity<String> responseEntity = trainingController.addTraining(requestBody, username, password);

        assertEquals("Training has been added", responseEntity.getBody());
        verify(trainingService).create(requestBody);
    }
}

