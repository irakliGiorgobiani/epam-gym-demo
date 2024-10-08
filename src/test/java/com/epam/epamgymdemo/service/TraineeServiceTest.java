package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.converter.TraineeToTraineeDtoConverter;
import com.epam.epamgymdemo.converter.TrainerToTrainerDtoConverter;
import com.epam.epamgymdemo.converter.TrainingToTraineeTrainingDtoConverter;
import com.epam.epamgymdemo.epamgymreporter.messaging.ReporterTrainingProducer;
import com.epam.epamgymdemo.metrics.CustomMetrics;
import com.epam.epamgymdemo.model.bo.Trainee;
import com.epam.epamgymdemo.model.bo.Trainer;
import com.epam.epamgymdemo.model.bo.Training;
import com.epam.epamgymdemo.model.bo.TrainingType;
import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.model.dto.TraineeDto;
import com.epam.epamgymdemo.model.dto.TraineeTrainingDto;
import com.epam.epamgymdemo.model.dto.TrainerDto;
import com.epam.epamgymdemo.repository.TraineeRepository;
import com.epam.epamgymdemo.repository.TrainingRepository;
import com.epam.epamgymdemo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomMetrics customMetrics;

    @Mock
    private ReporterTrainingProducer reporterTrainingProducer;

    @Mock
    private TraineeToTraineeDtoConverter traineeToTraineeDtoConverter;

    @Mock
    private TrainingToTraineeTrainingDtoConverter trainingToTraineeTrainingDtoConverter;

    @Mock
    private TrainerToTrainerDtoConverter trainerToTrainerDtoConverter;

    @InjectMocks
    private TraineeService traineeService;

    @Test
    void testCreate() {
        User user = User.builder().id(1L).username("username").build();
        TraineeDto traineeDto = TraineeDto.builder().birthday(LocalDate.now()).address("address").build();
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        traineeService.create(1L,traineeDto);

        verify(traineeRepository).save(any(Trainee.class));
    }

    @Test
    void testUpdate() {
        User user = User.builder()
                .username("username")
                .firstName("george")
                .lastName("russell")
                .id(1L)
                .build();
        Trainee trainee = Trainee.builder()
                .id(1L)
                .address("address")
                .trainers(new HashSet<>())
                .birthday(LocalDate.now())
                .user(user)
                .build();
        TraineeDto traineeDto = TraineeDto.builder()
                .firstName("firstName")
                .lastName("lastName")
                .address("New Address")
                .isActive(true)
                .build();
        user.setTrainee(trainee);

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        traineeService.update(user.getUsername(), traineeDto);

        assertEquals("New Address", trainee.getAddress());
    }

    @Test
    void testGetAll() {
        traineeService.getAll();

        verify(traineeRepository).findAll();
    }

    @Test
    void testGet() {
        String username = "username";

        Set<TrainerDto> trainers = new HashSet<>();

        Trainee trainee = Trainee.builder()
                .birthday(LocalDate.now())
                .address("address")
                .trainers(new HashSet<>())
                .build();

        User user = User.builder().username(username).trainee(trainee).build();

        trainee.setUser(user);

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        when(traineeToTraineeDtoConverter.convert(trainee, trainers)).thenReturn(TraineeDto.builder().build());

        traineeService.get(username);

        verify(userRepository, times(2)).findByUsername(username);
    }

    @Test
    void testGetByUsername() {
        User user = User.builder().username("username").build();
        Trainee trainee = Trainee.builder().user(user).build();
        user.setTrainee(trainee);

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        Trainee selectedTrainee = traineeService.getByUsername(user.getUsername());

        assertEquals(trainee, selectedTrainee);
    }

    @Test
    void testDeleteByUsername() {
        User user = User.builder().username("username").id(1L).build();
        Trainee trainee = Trainee.builder().id(1L).user(user).trainers(new HashSet<>()).build();
        user.setTrainee(trainee);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        traineeService.delete(user.getUsername());

        verify(traineeRepository).deleteById(trainee.getId());
        verify(userRepository).deleteById(user.getId());
    }

    @Test
    void testGetTrainingsByUsernameAndCriteria() {
        User user = User.builder()
                .username("username")
                .build();
        User userTrainer = User.builder()
                .firstName("username")
                .lastName("lastName")
                .build();
        Trainee trainee = Trainee.builder()
                .id(1L)
                .user(user)
                .build();
        Trainer trainer = Trainer.builder()
                .user(userTrainer)
                .build();
        Training training = Training.builder()
                .trainer(trainer)
                .trainingType(TrainingType.builder()
                        .typeName("name")
                        .build()).trainingDate(LocalDate.now())
                .trainee(trainee)
                .build();
        TraineeTrainingDto traineeTrainingDto = TraineeTrainingDto.builder()
                .trainingName(training.getTrainingName())
                .build();
        user.setTrainee(trainee);

        when(trainingRepository.findTrainings(user.getUsername(),
                LocalDate.of(2020, 1, 1),
                null, null, null)).thenReturn(List.of(training));
        when(trainingToTraineeTrainingDtoConverter.convert(training)).thenReturn(traineeTrainingDto);

        List<TraineeTrainingDto> trainingsByUsernameAndCriteria = traineeService
                .getTrainingsByUsernameAndCriteria(user.getUsername(),
                LocalDate.of(2020, 1, 1), null, null, null);

        assertEquals(training.getTrainingName(), trainingsByUsernameAndCriteria.get(0).getTrainingName());
        assertEquals(1, trainingsByUsernameAndCriteria.size());
    }

    @Test
    void testGetTrainerUnassignedToTrainee() {
        User user = User.builder()
                .username("username")
                .firstName("firstname")
                .lastName("lastName")
                .isActive(true)
                .build();
        User userTrainer = User.builder()
                .username("username")
                .firstName("firstname")
                .lastName("lastName")
                .isActive(true)
                .build();
        Trainee trainee = Trainee.builder()
                .id(1L)
                .user(user)
                .trainers(new HashSet<>())
                .build();
        Trainer trainer1 =  Trainer.builder()
                .user(userTrainer)
                .trainingType(TrainingType.builder()
                        .id(1L)
                        .build())
                .build();
        TrainerDto trainerDto = TrainerDto.builder()
                .firstName(userTrainer.getFirstName())
                .lastName(userTrainer.getLastName())
                .isActive(userTrainer.getIsActive())
                .username(userTrainer.getUsername())
                .specializationId(trainer1.getTrainingType().getId())
                .build();

        user.setTrainee(trainee);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        trainee.getTrainers().add(trainer1);
        when(trainerToTrainerDtoConverter.convert(trainer1)).thenReturn(trainerDto);

        Set<TrainerDto> trainersUnassignedToTrainee = traineeService.getUnassignedActiveTrainers(user.getUsername());

        assertEquals(trainer1.getUser().getUsername(),
                trainersUnassignedToTrainee.stream().toList().get(0).getUsername());
        assertEquals(1, trainersUnassignedToTrainee.size());
    }

    @Test
    void testAddTrainerToTraineesTrainersList() {
        User user = User.builder()
                .isActive(true)
                .firstName("firstName")
                .lastName("lastName")
                .username("username")
                .build();
        User userTrainer = User.builder()
                .username("username")
                .firstName("firstname")
                .lastName("lastName")
                .isActive(true)
                .build();
        Trainee trainee = Trainee.builder()
                .trainers(new HashSet<>())
                .id(1L).user(user)
                .build();
        Trainer trainer = Trainer.builder()
                .user(userTrainer)
                .trainingType(TrainingType.builder()
                        .id(1L)
                        .build())
                .trainees(new HashSet<>())
                .build();
        List<Trainer> trainers = List.of(trainer);
        user.setTrainee(trainee);

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        traineeService.addTrainerToTrainersList(trainee.getUser().getUsername(), trainers);

        assertEquals(1, trainee.getTrainers().size());
    }
}

