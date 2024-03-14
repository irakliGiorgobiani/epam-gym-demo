package com.epam.epamgymdemo.srvice;

import com.epam.epamgymdemo.model.bo.Trainee;
import com.epam.epamgymdemo.model.bo.Trainer;
import com.epam.epamgymdemo.model.bo.Training;
import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.repository.TraineeRepository;
import com.epam.epamgymdemo.repository.TrainerRepository;
import com.epam.epamgymdemo.repository.TrainingRepository;
import com.epam.epamgymdemo.repository.UserRepository;
import com.epam.epamgymdemo.service.TraineeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TraineeService traineeService;

    @Test
    void testCreate() throws InstanceNotFoundException {
        User user = User.builder().build();

        Trainee trainee = traineeService.create(LocalDate.now(), "address", user);

        verify(traineeRepository).save(trainee);
    }

    @Test
    void testUpdate() throws InstanceNotFoundException {
        User user = User.builder().id(1L).build();
        Trainee trainee = Trainee.builder().id(1L).address("address").birthday(LocalDate.now()).user(user).build();

        when(traineeRepository.findById(1L)).thenReturn(Optional.ofNullable(trainee));
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        traineeService.update(1L, LocalDate.of(2024, 12, 1), "New Address", 1L);

        assertNotNull(trainee);
        assertEquals("New Address", trainee.getAddress());
    }

    @Test
    void testGetById() throws InstanceNotFoundException {
        Trainee trainee = Trainee.builder().id(1L).build();

        when(traineeRepository.findById(1L)).thenReturn(Optional.ofNullable(trainee));

        traineeService.getById(1L);

        verify(traineeRepository).findById(1L);
    }

    @Test
    void testGetAll() {
        traineeService.getAll();

        verify(traineeRepository).findAll();
    }

    @Test
    void testDeleteById() throws InstanceNotFoundException {
        User user = User.builder().id(1L).build();
        Trainee trainee = Trainee.builder().user(user).id(2L).build();

        when(traineeRepository.findById(2L)).thenReturn(Optional.ofNullable(trainee));

        traineeService.deleteById(2L);

        verify(traineeRepository).deleteById(2L);
    }

    @Test
    void testGetByUsername() throws InstanceNotFoundException {
        User user = User.builder().username("username").build();
        Trainee trainee = Trainee.builder().user(user).build();

        when(userRepository.findByUsername("username")).thenReturn(user);
        when(traineeRepository.findAll()).thenReturn(List.of(trainee));

        Trainee selectedTrainee = traineeService.getByUsername(user.getUsername());

        assertEquals(trainee, selectedTrainee);
    }

    @Test
    void testDeleteByUsername() throws InstanceNotFoundException {
        User user = User.builder().username("username").build();
        Trainee trainee = Trainee.builder().id(1L).user(user).build();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(traineeRepository.findAll()).thenReturn(List.of(trainee));
        when(traineeRepository.findById(1L)).thenReturn(Optional.of(trainee));

        traineeService.deleteByUsername(user.getUsername());

        verify(traineeRepository).deleteById(1L);
    }

    @Test
    void testGetTrainingsByUsernameAndCriteria() throws InstanceNotFoundException {
        User user = User.builder().username("username").build();
        Trainee trainee = Trainee.builder().id(1L).user(user).build();
        Training training = Training.builder().trainingDate(LocalDate.now()).trainee(trainee).build();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(traineeRepository.findAll()).thenReturn(List.of(trainee));
        when(trainingRepository.findAll()).thenReturn(List.of(training));

        List<Training> trainingsByUsernameAndCriteria = traineeService.getTrainingsByUsernameAndCriteria(user.getUsername(),
                LocalDate.of(2020, 1, 1), null, null, null);

        assertEquals(training, trainingsByUsernameAndCriteria.get(0));
        assertEquals(1, trainingsByUsernameAndCriteria.size());
    }

    @Test
    void testGetTrainerUnassignedToTrainee() throws InstanceNotFoundException {
        User user = User.builder().username("username").build();
        Trainee trainee = Trainee.builder().id(1L).user(user).trainers(new HashSet<>()).build();
        Trainer trainer1 = Trainer.builder().build();
        Trainer trainer2 = Trainer.builder().build();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(traineeRepository.findAll()).thenReturn(List.of(trainee));
        when(trainerRepository.findAll()).thenReturn(List.of(trainer1, trainer2));
        trainee.getTrainers().add(trainer1);

        List<Trainer> trainersUnassignedToTrainee = traineeService.getUnassignedActiveTrainers(user.getUsername());

        assertEquals(trainer2, trainersUnassignedToTrainee.get(0));
        assertEquals(1, trainersUnassignedToTrainee.size());
    }

    @Test
    void testAddTrainerToTraineesTrainersList() throws InstanceNotFoundException {
        Trainee trainee = Trainee.builder().trainers(new HashSet<>()).id(1L).build();
        Trainer trainer = Trainer.builder().trainees(new HashSet<>()).build();

        when(traineeRepository.findById(1L)).thenReturn(Optional.ofNullable(trainee));

        traineeService.addTrainerToTrainersList(1L, trainer);

        assertEquals(1, trainee.getTrainers().size());
    }

    @Test
    void testRemoveTrainerFromTraineesTrainersList() throws InstanceNotFoundException {
        Trainee trainee = Trainee.builder().id(1L).trainers(new HashSet<>()).build();
        Trainer trainer = Trainer.builder().trainees(new HashSet<>()).build();

        when(traineeRepository.findById(1L)).thenReturn(Optional.ofNullable(trainee));
        trainee.getTrainers().add(trainer);
        trainer.getTrainees().add(trainee);

        traineeService.deleteTrainerFromTrainersList(1L, trainer);

        assertEquals(0, trainee.getTrainers().size());
    }
}

