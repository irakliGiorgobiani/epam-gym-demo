package com.epam.epamgymdemo.controller;

import com.epam.epamgymdemo.model.dto.TrainingTypeDto;
import com.epam.epamgymdemo.service.AuthenticationService;
import com.epam.epamgymdemo.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import javax.security.auth.login.CredentialNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrainingTypeControllerTest {

    @InjectMocks
    private TrainingTypeController trainingTypeController;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private TrainingTypeService trainingTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() throws CredentialNotFoundException {
        String username = "testUser";
        String password = "password";

        List<TrainingTypeDto> trainingTypeList = new ArrayList<>();
        TrainingTypeDto trainingTypeDto1 = new TrainingTypeDto();
        trainingTypeDto1.setId(1L);
        trainingTypeDto1.setTypeName("Training Type 1");
        TrainingTypeDto trainingTypeDto2 = new TrainingTypeDto();
        trainingTypeDto2.setId(2L);
        trainingTypeDto2.setTypeName("Training Type 2");
        trainingTypeList.add(trainingTypeDto1);
        trainingTypeList.add(trainingTypeDto2);

        doNothing().when(authenticationService).authenticateUser(username, password);
        when(trainingTypeService.getAll()).thenReturn(trainingTypeList);

        ResponseEntity<List<TrainingTypeDto>> responseEntity = trainingTypeController.getAll(username, password);

        assertEquals(2, Objects.requireNonNull(responseEntity.getBody()).size());
        assertEquals("Training Type 1", responseEntity.getBody().get(0).getTypeName());
        assertEquals("Training Type 2", responseEntity.getBody().get(1).getTypeName());
        verify(trainingTypeService).getAll();
    }
}

