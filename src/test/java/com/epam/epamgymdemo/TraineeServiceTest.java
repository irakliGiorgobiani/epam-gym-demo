package com.epam.epamgymdemo;

import com.epam.epamgymdemo.model.Trainee;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.model.User;
import com.epam.epamgymdemo.repository.TraineeRepository;
import com.epam.epamgymdemo.repository.TrainerRepository;
import com.epam.epamgymdemo.repository.TrainingRepository;
import com.epam.epamgymdemo.service.TraineeService;
import com.epam.epamgymdemo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TraineeServiceTest {
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private UserService userService;
    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private TrainerRepository trainerRepository;
    private TraineeService traineeService;
    private Long traineeId;
    private LocalDate birthday;
    private String address;
    private Long userId;
    private String firstName;
    private String lastName;
    private User user;
    private Trainee trainee;
    private Training training;
    private Trainer trainer1;
    private Trainer trainer2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        traineeService = new TraineeService(traineeRepository, trainerRepository, trainingRepository, userService);

        traineeId = 1L;
        birthday = LocalDate.of(2000, 1, 1);
        address = "Address";
        userId = 1L;
        firstName = "John";
        lastName = "Doe";

        user = User.builder()
                .id(userId)
                .firstName(firstName)
                .lastName(lastName)
                .isActive(true)
                .build();

        trainee = Trainee.builder()
                .id(traineeId)
                .birthday(birthday)
                .address(address)
                .user(user)
                .trainers(new HashSet<>())
                .build();

        training = Training.builder()
                .trainee(trainee)
                .trainingDate(LocalDate.of(2024, 2, 1))
                .build();

        trainer1 = Trainer.builder()
                .trainees(new HashSet<>())
                .build();
        trainer2 = Trainer.builder().build();
    }

    @Test
    void testCreateTrainee() throws InstanceNotFoundException {
        when(userService.getById(userId)).thenReturn(user);
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.ofNullable(trainee));

        traineeService.create(birthday, address, firstName, lastName, true);

        assertEquals(userService.getById(userId), Objects.requireNonNull(traineeRepository.findById(traineeId).orElse(null)).getUser());
        assertEquals(traineeId, Objects.requireNonNull(traineeRepository.findById(traineeId).orElse(null)).getId());
    }

    @Test
    void testUpdateTrainee() throws InstanceNotFoundException {
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.ofNullable(trainee));

        traineeService.update(traineeId, LocalDate.of(2024, 12, 1), "New Address", userId);

        assertEquals("New Address", trainee.getAddress());
    }

    @Test
    void testSelectTrainee() throws InstanceNotFoundException {
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.ofNullable(trainee));

        Trainee newTrainee = traineeService.getById(traineeId);

        assertEquals(traineeRepository.findById(traineeId).orElse(null), newTrainee);
    }

    @Test
    void testSelectAllTrainees() {
        when(traineeRepository.findAll()).thenReturn(List.of(trainee));

        List<Trainee> trainees = traineeService.getAll();

        assertEquals(traineeRepository.findAll(), trainees);
    }

    @Test
    void testDeleteTrainee() {
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.ofNullable(trainee));

        traineeService.deleteById(traineeId);

        verify(traineeRepository, times(1)).deleteById(traineeId);
    }

    @Test
    void testChangePassword() throws InstanceNotFoundException {
        String newPassword = "newPassword";

        when(traineeRepository.findById(traineeId)).thenReturn(Optional.ofNullable(trainee));
        when(userService.getById(userId)).thenReturn(user);

        traineeService.changePassword(traineeId, newPassword);

        verify(userService, times(1)).changePassword(userId, newPassword);
    }

    @Test
    void testChangeIsActive() throws InstanceNotFoundException {
        Boolean isActive = false;

        when(traineeRepository.findById(traineeId)).thenReturn(Optional.ofNullable(trainee));
        when(userService.getById(userId)).thenReturn(user);

        traineeService.changeIsActive(traineeId, isActive);

        verify(userService, times(1)).changeIsActive(userId, isActive);
    }

    @Test
    void testGetByUsername() throws InstanceNotFoundException {
        when(userService.getByUsername(user.getUsername())).thenReturn(user);
        when(traineeRepository.findAll()).thenReturn(List.of(trainee));

        Trainee selectedTrainee = traineeService.getByUsername(user.getUsername());

        assertEquals(trainee, selectedTrainee);
    }

    @Test
    void testDeleteByUsername() throws InstanceNotFoundException {
        when(userService.getByUsername(user.getUsername())).thenReturn(user);

        traineeService.deleteByUsername(user.getUsername());

        verify(traineeRepository, times(1)).deleteById(userId);
    }

    @Test
    void testGetTrainingsByUsernameAndCriteria() throws InstanceNotFoundException {
        when(userService.getByUsername(user.getUsername())).thenReturn(user);
        when(traineeRepository.findAll()).thenReturn(List.of(trainee));
        when(trainingRepository.findAll()).thenReturn(List.of(training));

        List<Training> trainingsByUsernameAndCriteria = traineeService.getTrainingsByUsernameAndCriteria(user.getUsername(),
                LocalDate.of(2020, 1, 1), null, null, null);

        assertEquals(training, trainingsByUsernameAndCriteria.get(0));
        assertEquals(1, trainingsByUsernameAndCriteria.size());
    }

    @Test
    void testGetTrainerUnassignedToTrainee() throws InstanceNotFoundException {
        when(userService.getByUsername(user.getUsername())).thenReturn(user);
        when(traineeRepository.findAll()).thenReturn(List.of(trainee));
        when(trainerRepository.findAll()).thenReturn(List.of(trainer1, trainer2));
        trainee.getTrainers().add(trainer1);

        List<Trainer> trainersUnassignedToTrainee = traineeService.getUnassignedTrainers(user.getUsername());

        assertEquals(trainer2, trainersUnassignedToTrainee.get(0));
        assertEquals(1, trainersUnassignedToTrainee.size());
    }

    @Test
    void testAddTrainerToTraineesTrainersList() throws InstanceNotFoundException {
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.ofNullable(trainee));

        traineeService.addTrainerToTrainersList(traineeId, trainer1);

        assertEquals(1, trainee.getTrainers().size());
    }

    @Test
    void testRemoveTrainerFromTraineesTrainersList() throws InstanceNotFoundException {
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.ofNullable(trainee));
        trainee.getTrainers().add(trainer1);
        trainer1.getTrainees().add(trainee);

        traineeService.removeTrainerFromTrainersList(traineeId, trainer1);

        assertEquals(0, trainee.getTrainers().size());
    }
}

