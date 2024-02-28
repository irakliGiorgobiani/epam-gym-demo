package com.epam.epamgymdemo;

import com.epam.epamgymdemo.dao.TraineeDao;
import com.epam.epamgymdemo.dao.TrainerDao;
import com.epam.epamgymdemo.dao.TrainingDao;
import com.epam.epamgymdemo.dao.UserDao;
import com.epam.epamgymdemo.generator.UsernamePasswordGenerator;
import com.epam.epamgymdemo.model.Trainee;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.model.User;
import com.epam.epamgymdemo.service.TraineeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TraineeServiceTest {

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private UserDao userDao;

    @Mock
    private TrainingDao trainingDao;

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private UsernamePasswordGenerator usernamePasswordGenerator;

    private TraineeService traineeService;

    private Long traineeId;
    private LocalDate dateOfBirth;
    private String address;
    private Long userId;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private User user;
    private Trainee trainee;
    private Training training;
    private Trainer trainer1;
    private Trainer trainer2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        traineeService = new TraineeService(traineeDao, userDao, trainingDao, trainerDao, usernamePasswordGenerator);

        traineeId = 1L;
        dateOfBirth = LocalDate.of(2000, 1, 1);
        address = "Address";
        userId = 1L;
        firstName = "John";
        lastName = "Doe";
        isActive = true;

        user = User.builder()
                .id(userId)
                .firstName(firstName)
                .lastName(lastName)
                .isActive(isActive)
                .userName(usernamePasswordGenerator.generateUsername(firstName, lastName))
                .password(usernamePasswordGenerator.generatePassword())
                .build();

        trainee = Trainee.builder()
                .id(traineeId)
                .dateOfBirth(dateOfBirth)
                .address(address)
                .user(user)
                .trainers(new HashSet<>())
                .build();

        training = Training.builder()
                .trainee(trainee)
                .trainingDate(LocalDate.of(2024, 2, 1))
                .build();

        trainer1 = Trainer.builder().build();
        trainer2 = Trainer.builder().build();
    }

    @Test
    void testCreateTrainee() throws InstanceNotFoundException {
        when(userDao.get(userId)).thenReturn(user);
        when(traineeDao.get(traineeId)).thenReturn(trainee);

        traineeService.createTrainee(traineeId, dateOfBirth, address, userId, firstName, lastName, isActive);

        assertEquals(userDao.get(userId), traineeDao.get(traineeId).getUser());
        assertEquals(traineeId, traineeDao.get(traineeId).getId());
    }

    @Test
    void testUpdateTrainee() throws InstanceNotFoundException {
        when(traineeDao.get(traineeId)).thenReturn(trainee);

        traineeService.updateTrainee(traineeId, LocalDate.of(2024, 12, 1), "New Address", userId);

        assertEquals("New Address", trainee.getAddress());
    }

    @Test
    void testSelectTrainee() throws InstanceNotFoundException {
        when(traineeDao.get(traineeId)).thenReturn(trainee);

        Trainee newTrainee = traineeService.selectTrainee(traineeId);

        assertEquals(traineeDao.get(traineeId), newTrainee);
    }

    @Test
    void testSelectAllTrainees() {
        when(traineeDao.getAll()).thenReturn(List.of(trainee));

        List<Trainee> trainees = traineeService.selectAllTrainees();

        assertEquals(traineeDao.getAll(), trainees);
    }

    @Test
    void testDeleteTrainee() throws InstanceNotFoundException {
        when(traineeDao.get(traineeId)).thenReturn(trainee);

        traineeService.deleteTrainee(traineeId);

        verify(traineeDao, times(1)).delete(traineeId);
    }

    @Test
    void testChangePassword() throws InstanceNotFoundException {
        String newPassword = "newPassword";

        when(traineeDao.get(traineeId)).thenReturn(trainee);
        when(userDao.get(userId)).thenReturn(user);

        traineeService.changePassword(traineeId, newPassword);

        verify(userDao, times(1)).updatePassword(userId, newPassword);
    }

    @Test
    void testChangeIsActive() throws InstanceNotFoundException {
        Boolean isActive = false;

        when(traineeDao.get(traineeId)).thenReturn(trainee);
        when(userDao.get(userId)).thenReturn(user);

        traineeService.changeIsActive(traineeId, isActive);

        verify(userDao, times(1)).updateIsActive(userId, isActive);
    }

    @Test
    void testGetByUsername() throws InstanceNotFoundException {
        when(userDao.getByUsername(user.getUserName())).thenReturn(user);
        when(traineeDao.getAll()).thenReturn(List.of(trainee));

        Trainee selectedTrainee = traineeService.getByUsername(user.getUserName());

        assertEquals(trainee, selectedTrainee);
    }

    @Test
    void testDeleteByUsername() throws InstanceNotFoundException {
        when(userDao.getByUsername(user.getUserName())).thenReturn(user);

        traineeService.deleteByUsername(user.getUserName());

        verify(traineeDao, times(1)).delete(userId);
    }

    @Test
    void testGetTrainingsByUsernameAndCriteria() throws InstanceNotFoundException {
        when(userDao.getByUsername(user.getUserName())).thenReturn(user);
        when(traineeDao.getAll()).thenReturn(List.of(trainee));
        when(trainingDao.getAll()).thenReturn(List.of(training));

        List<Training> trainingsByUsernameAndCriteria = traineeService.getTrainingsByUsernameAndCriteria(user.getUserName(),
                LocalDate.of(2020, 1, 1), null, null, null);

        assertEquals(training, trainingsByUsernameAndCriteria.get(0));
        assertEquals(1, trainingsByUsernameAndCriteria.size());
    }

    @Test
    void testGetTrainerUnassignedToTrainee() throws InstanceNotFoundException {
        when(userDao.getByUsername(user.getUserName())).thenReturn(user);
        when(traineeDao.getAll()).thenReturn(List.of(trainee));
        when(trainerDao.getAll()).thenReturn(List.of(trainer1, trainer2));
        trainee.getTrainers().add(trainer1);

        List<Trainer> trainersUnassignedToTrainee = traineeService.getTrainersUnassignedToTrainee(user.getUserName());

        assertEquals(trainer2, trainersUnassignedToTrainee.get(0));
        assertEquals(1, trainersUnassignedToTrainee.size());
    }

    @Test
    void testAddTrainerToTraineesTrainersList() throws InstanceNotFoundException {
        when(traineeDao.get(traineeId)).thenReturn(trainee);

        traineeService.addTrainerToTraineesTrainersList(traineeId, trainer1);

        assertEquals(1, trainee.getTrainers().size());
    }

    @Test
    void testRemoveTrainerFromTraineesTrainersList() throws InstanceNotFoundException {
        when(traineeDao.get(traineeId)).thenReturn(trainee);
        trainee.getTrainers().add(trainer1);

        traineeService.removeTrainerFromTraineesTrainersList(traineeId, trainer1);

        assertEquals(0, trainee.getTrainers().size());
    }
}

