package com.epam.epamgymdemo.srvice;

import com.epam.epamgymdemo.model.bo.Trainee;
import com.epam.epamgymdemo.model.bo.Trainer;
import com.epam.epamgymdemo.model.bo.Training;
import com.epam.epamgymdemo.model.bo.TrainingType;
import com.epam.epamgymdemo.repository.TrainingRepository;
import com.epam.epamgymdemo.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TrainingService trainingService;

    @Test
    void testCreate() throws InstanceNotFoundException {
        Trainee trainee = Trainee.builder().trainers(new HashSet<>()).build();
        Trainer trainer = Trainer.builder().trainees(new HashSet<>()).build();
        TrainingType trainingType = TrainingType.builder().build();

        Training training = trainingService.create("yoga_class", LocalDate.now(), 60, trainee, trainer, trainingType);

        ArgumentCaptor<Training> trainingArgumentCaptor = ArgumentCaptor.forClass(Training.class);
        verify(trainingRepository).save(trainingArgumentCaptor.capture());

        Training capturedTraining = trainingArgumentCaptor.getValue();

        assertEquals(training, capturedTraining);
    }

    @Test
    void testGet() throws InstanceNotFoundException {
        Training training = Training.builder()
                .id(1L)
                .build();

        when(trainingRepository.findById(1L)).thenReturn(Optional.ofNullable(training));

        Training selectedTraining = trainingService.getById(1L);

        assertEquals(training, selectedTraining);
    }

    @Test
    void testGetAll() {
        trainingService.getAll();

        verify(trainingRepository).findAll();
    }
}

