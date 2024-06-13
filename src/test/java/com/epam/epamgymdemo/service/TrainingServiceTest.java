package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.epamgymreporter.messaging.ReporterTrainingProducer;
import com.epam.epamgymdemo.model.bo.Trainee;
import com.epam.epamgymdemo.model.bo.Trainer;
import com.epam.epamgymdemo.model.bo.Training;
import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.model.dto.TrainingDto;
import com.epam.epamgymdemo.repository.TrainingRepository;
import com.epam.epamgymdemo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReporterTrainingProducer reporterTrainingProducer;

    @InjectMocks
    private TrainingService trainingService;

    @Test
    void testCreate() {
        User user = User.builder().username("username").build();
        Trainee trainee = Trainee.builder().id(1L).user(user).trainers(new HashSet<>()).build();
        Trainer trainer = Trainer.builder().user(user).trainees(new HashSet<>()).build();
        user.setTrainee(trainee);
        user.setTrainer(trainer);
        TrainingDto trainingDto = TrainingDto.builder()
                .trainingDate(LocalDate.now())
                .trainerUsername(trainer.getUser().getUsername())
                .traineeUsername(trainee.getUser().getUsername())
                .trainingDuration(12)
                .trainingName("training")
                .build();

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        trainingService.create(trainingDto);

        verify(trainingRepository).save(any(Training.class));
    }

    @Test
    void testGetAll() {
        trainingService.getAll();

        verify(trainingRepository).findAll();
    }
}