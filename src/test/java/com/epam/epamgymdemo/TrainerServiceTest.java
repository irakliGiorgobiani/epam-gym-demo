package com.epam.epamgymdemo;

import com.epam.epamgymdemo.dao.TrainerDao;
import com.epam.epamgymdemo.dao.TrainingDao;
import com.epam.epamgymdemo.dao.TrainingTypeDao;
import com.epam.epamgymdemo.dao.UserDao;
import com.epam.epamgymdemo.generator.UsernamePasswordGenerator;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.model.TrainingType;
import com.epam.epamgymdemo.model.User;
import com.epam.epamgymdemo.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.management.InstanceNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TrainerServiceTest {

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private UserDao userDao;

    @Mock
    private TrainingTypeDao trainingTypeDao;

    @Mock
    private TrainingDao trainingDao;

    @Mock
    private UsernamePasswordGenerator usernamePasswordGenerator;

    @InjectMocks
    private TrainerService trainerService;

    private Long trainerId;
    private Long typeId;
    private Long userId;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private User user;
    private TrainingType trainingType;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainerService = new TrainerService(trainerDao, userDao, trainingTypeDao, trainingDao, usernamePasswordGenerator);

        trainerId = 1L;
        typeId = 1L;
        userId = 1L;
        firstName = "John";
        lastName = "Doe";
        isActive = true;

        user = User.builder()
                .id(userId)
                .firstName(firstName)
                .lastName(lastName)
                .isActive(isActive)
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
        when(usernamePasswordGenerator.generateUsername(firstName, lastName)).thenReturn("username");
        when(usernamePasswordGenerator.generatePassword()).thenReturn("password");
        when(trainingTypeDao.get(typeId)).thenReturn(trainingType);

        trainerService.createTrainer(typeId, firstName, lastName, isActive);

        verify(userDao, times(1)).create(any(User.class));
        verify(trainerDao, times(1)).create(any(Trainer.class));
    }

    @Test
    void testUpdateTrainer() throws InstanceNotFoundException {
        when(trainerDao.get(trainerId)).thenReturn(trainer);

        trainerService.updateTrainer(trainerId, typeId, userId);

        verify(trainerDao, times(1)).update(any(Trainer.class));
    }

    @Test
    void testSelectTrainer() throws InstanceNotFoundException {
        when(trainerDao.get(trainerId)).thenReturn(trainer);

        Trainer selectedTrainer = trainerService.selectTrainer(trainerId);

        assertEquals(trainer, selectedTrainer);
    }

    @Test
    void testSelectAllTrainers() {
        when(trainerDao.getAll()).thenReturn(List.of(trainer));

        List<Trainer> trainers = trainerService.selectAllTrainers();

        assertEquals(List.of(trainer), trainers);
    }

    @Test
    void testChangePassword() throws InstanceNotFoundException {
        String newPassword = "newPassword";

        when(trainerDao.get(trainerId)).thenReturn(trainer);
        when(userDao.get(userId)).thenReturn(user);

        trainerService.changePassword(trainerId, newPassword);

        verify(userDao, times(1)).updatePassword(userId, newPassword);
    }

    @Test
    void testChangeIsActive() throws InstanceNotFoundException {
        Boolean newIsActive = false;

        when(trainerDao.get(trainerId)).thenReturn(trainer);
        when(userDao.get(userId)).thenReturn(user);

        trainerService.changeIsActive(trainerId, newIsActive);

        verify(userDao, times(1)).updateIsActive(userId, newIsActive);
    }

    @Test
    void testGetByUsername() throws InstanceNotFoundException {
        when(userDao.getByUsername("username")).thenReturn(user);
        when(trainerDao.getAll()).thenReturn(List.of(trainer));

        Trainer selectedTrainer = trainerService.getByUsername("username");

        assertEquals(trainer, selectedTrainer);
    }

    @Test
    void testGetTrainingsByUsernameAndCriteria() throws InstanceNotFoundException {
        when(userDao.getByUsername("username")).thenReturn(user);
        when(trainerDao.getAll()).thenReturn(List.of(trainer));
        when(trainingDao.getAll()).thenReturn(List.of());

        List<Training> trainings = trainerService.getTrainingsByUsernameAndCriteria("username", null, null, null);

        assertEquals(0, trainings.size());
    }
}
