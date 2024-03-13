package com.epam.epamgymdemo.srvice;

import com.epam.epamgymdemo.model.bo.Trainer;
import com.epam.epamgymdemo.model.bo.Training;
import com.epam.epamgymdemo.model.bo.TrainingType;
import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.repository.TrainerRepository;
import com.epam.epamgymdemo.repository.TrainingRepository;
import com.epam.epamgymdemo.repository.TrainingTypeRepository;
import com.epam.epamgymdemo.repository.UserRepository;
import com.epam.epamgymdemo.service.TrainerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    void testCreate() throws InstanceNotFoundException {
        User user = User.builder().build();

        Long typeId = 3L;
        TrainingType trainingType = TrainingType.builder().id(typeId).build();

        when(trainingTypeRepository.findById(typeId)).thenReturn(Optional.ofNullable(trainingType));

        Trainer createdTrainer = trainerService.create(typeId, user);

        verify(trainerRepository).save(createdTrainer);
    }

    @Test
    void testUpdate() throws InstanceNotFoundException {
        Trainer trainer = Trainer.builder().id(1L).build();

        when(trainerRepository.findById(1L)).thenReturn(Optional.ofNullable(trainer));

        trainerService.update(1L, null, null);

        assertNotNull(trainer);
        verify(trainerRepository).save(trainer);
    }

    @Test
    void testGet() throws InstanceNotFoundException {
        Trainer trainer = Trainer.builder().id(5L).build();

        when(trainerRepository.findById(5L)).thenReturn(Optional.ofNullable(trainer));

        Trainer selectedTrainer = trainerService.getById(5L);

        assertEquals(trainer, selectedTrainer);
    }

    @Test
    void testGetAll() {
        trainerService.getAll();

        verify(trainerRepository).findAll();
    }

    @Test
    void testGetTrainingsByUsernameAndCriteria() throws InstanceNotFoundException {
        User user = User.builder().username("username").build();
        Trainer trainer = Trainer.builder().user(user).build();
        Training training = Training.builder().trainer(trainer).build();

        when(userRepository.findByUsername("username")).thenReturn(user);
        when(trainerRepository.findAll()).thenReturn(List.of(trainer));
        when(trainingRepository.findAll()).thenReturn(List.of(training));

        List<Training> trainings = trainerService.getTrainingsByUsernameAndCriteria("username", null, null, null);

        assertEquals(1, trainings.size());
    }
}
