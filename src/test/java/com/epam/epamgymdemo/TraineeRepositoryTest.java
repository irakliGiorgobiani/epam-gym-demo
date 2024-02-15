package com.epam.epamgymdemo;

import com.epam.epamgymdemo.model.Trainee;
import com.epam.epamgymdemo.repository.TraineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TraineeRepositoryTest {

    private TraineeRepository traineeRepository;

    @BeforeEach
    void setUp() {
        traineeRepository = new TraineeRepository();
    }

    @Test
    void testGetTrainee() {
        Trainee trainee = new Trainee(1L, LocalDate.now(), "123 Street", 1L);
        traineeRepository.addTrainee(trainee);

        Trainee retrievedTrainee = traineeRepository.getTrainee(1L);

        assertNotNull(retrievedTrainee);

        assertEquals(trainee.getTraineeId(), retrievedTrainee.getTraineeId());
    }

    @Test
    void testGetAllTrainees() {
        Trainee trainee1 = new Trainee(1L, LocalDate.now(), "123 Street", 1L);
        Trainee trainee2 = new Trainee(2L, LocalDate.now(), "456 Street", 2L);
        traineeRepository.addTrainee(trainee1);
        traineeRepository.addTrainee(trainee2);

        List<Trainee> allTrainees = traineeRepository.getALlTrainees();

        assertEquals(2, allTrainees.size());
    }

    @Test
    void testAddTrainee() {
        Trainee trainee = new Trainee(1L, LocalDate.now(), "Address 1", 1L);
        traineeRepository.addTrainee(trainee);

        assertTrue(traineeRepository.containsTrainee(1L));
        assertEquals(trainee, traineeRepository.getTrainee(1L));
    }

    @Test
    void testUpdateTrainee() {
        Trainee trainee = new Trainee(1L, LocalDate.now(), "Address 1", 1L);
        traineeRepository.addTrainee(trainee);

        Trainee updatedTrainee = new Trainee(1L, LocalDate.now(), "Updated Address", 1L);
       traineeRepository.updateTrainee(updatedTrainee);

        assertEquals(updatedTrainee, traineeRepository.getTrainee(1L));
    }

    @Test
    void testRemoveTrainee() {
        Trainee trainee = new Trainee(1L, LocalDate.now(), "Address 1", 1L);
        traineeRepository.addTrainee(trainee);

        traineeRepository.removeTrainee(1L);

        assertFalse(traineeRepository.containsTrainee(1L));
    }

    @Test
    void testContainsTraineeById() {
        Trainee trainee = new Trainee(1L, LocalDate.now(), "Address 1", 1L);
        traineeRepository.addTrainee(trainee);

        assertTrue(traineeRepository.containsTrainee(1L));
    }

    @Test
    void testContainsTraineeByObject() {
        Trainee trainee = new Trainee(1L, LocalDate.now(), "Address 1", 1L);
        traineeRepository.addTrainee(trainee);

        assertTrue(traineeRepository.containsTrainee(trainee));
    }
}

