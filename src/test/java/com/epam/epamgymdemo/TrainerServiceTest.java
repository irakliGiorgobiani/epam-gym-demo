package com.epam.epamgymdemo;

import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.model.TrainingType;
import com.epam.epamgymdemo.model.User;
import com.epam.epamgymdemo.repository.TrainerRepository;
import com.epam.epamgymdemo.repository.TrainingRepository;
import com.epam.epamgymdemo.service.TrainerService;
import com.epam.epamgymdemo.service.TrainingTypeService;
import com.epam.epamgymdemo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TrainerServiceTest {
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private UserService userService;
    @Mock
    private TrainingTypeService trainingTypeService;
    @Mock
    private TrainingRepository trainingRepository;
    @InjectMocks
    private TrainerService trainerService;
    private Long trainerId;
    private Long typeId;
    private Long userId;
    private String firstName;
    private String lastName;
    private User user;
    private TrainingType trainingType;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainerService = new TrainerService(trainerRepository, trainingRepository, trainingTypeService, userService);

        trainerId = 1L;
        typeId = 1L;
        userId = 1L;
        firstName = "John";
        lastName = "Doe";

        user = User.builder()
                .id(userId)
                .firstName(firstName)
                .lastName(lastName)
                .isActive(true)
                .build();

        trainingType = TrainingType.builder()
                .id(typeId)
                .typeName("TrainingType")
                .build();

        trainer = Trainer.builder()
                .id(trainerId)
                .trainingType(trainingType)
                .user(user)
                .build();
    }

    @Test
    void testCreateTrainer() throws InstanceNotFoundException {
        when(userService.getById(userId)).thenReturn(user);
        when(trainerRepository.findById(trainerId)).thenReturn(Optional.ofNullable(trainer));
        when(trainingTypeService.getById(typeId)).thenReturn(trainingType);

        trainerService.create(trainingType.getId(), firstName, lastName, true);

        assertEquals(userService.getById(userId), Objects.requireNonNull(trainerRepository.findById(trainerId).orElse(null)).getUser());
        assertEquals(trainerId, Objects.requireNonNull(trainerRepository.findById(trainerId).orElse(null)).getId());
    }

    @Test
    void testUpdateTrainer() throws InstanceNotFoundException {
        when(trainerRepository.findById(trainerId)).thenReturn(Optional.ofNullable(trainer));

        trainerService.update(trainerId, typeId, userId);

        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    void testSelectTrainer() throws InstanceNotFoundException {
        when(trainerRepository.findById(trainerId)).thenReturn(Optional.ofNullable(trainer));

        Trainer selectedTrainer = trainerService.getById(trainerId);

        assertEquals(trainer, selectedTrainer);
    }

    @Test
    void testSelectAllTrainers() {
        when(trainerRepository.findAll()).thenReturn(List.of(trainer));

        List<Trainer> trainers = trainerService.getAll();

        assertEquals(List.of(trainer), trainers);
    }

    @Test
    void testChangePassword() throws InstanceNotFoundException {
        String newPassword = "newPassword";

        when(trainerRepository.findById(trainerId)).thenReturn(Optional.ofNullable(trainer));
        when(userService.getById(userId)).thenReturn(user);

        trainerService.changePassword(trainerId, newPassword);

        verify(userService, times(1)).changePassword(userId, newPassword);
    }

    @Test
    void testChangeIsActive() throws InstanceNotFoundException {
        Boolean newIsActive = false;

        when(trainerRepository.findById(trainerId)).thenReturn(Optional.ofNullable(trainer));
        when(userService.getById(userId)).thenReturn(user);

        trainerService.changeIsActive(trainerId, newIsActive);

        verify(userService, times(1)).changeIsActive(userId, newIsActive);
    }

    @Test
    void testGetByUsername() throws InstanceNotFoundException {
        when(userService.getByUsername("username")).thenReturn(user);
        when(trainerRepository.findAll()).thenReturn(List.of(trainer));

        Trainer selectedTrainer = trainerService.getByUsername("username");

        assertEquals(trainer, selectedTrainer);
    }

    @Test
    void testGetTrainingsByUsernameAndCriteria() throws InstanceNotFoundException {
        when(userService.getByUsername("username")).thenReturn(user);
        when(trainerRepository.findAll()).thenReturn(List.of(trainer));
        when(trainingRepository.findAll()).thenReturn(List.of());

        List<Training> trainings = trainerService.getTrainingsByUsernameAndCriteria("username", null, null, null);

        assertEquals(0, trainings.size());
    }
}
