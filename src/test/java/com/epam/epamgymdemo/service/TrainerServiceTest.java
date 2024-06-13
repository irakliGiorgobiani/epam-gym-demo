package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.converter.BoToDtoConverter;
import com.epam.epamgymdemo.epamgymreporter.messaging.TrainingSummaryDtoConsumer;
import com.epam.epamgymdemo.metrics.CustomMetrics;
import com.epam.epamgymdemo.model.bo.Trainee;
import com.epam.epamgymdemo.model.bo.Trainer;
import com.epam.epamgymdemo.model.bo.Training;
import com.epam.epamgymdemo.model.bo.TrainingType;
import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.model.dto.TrainerDto;
import com.epam.epamgymdemo.model.dto.TrainerTrainingDto;
import com.epam.epamgymdemo.model.dto.TrainerWithListDto;
import com.epam.epamgymdemo.model.dto.UserDto;
import com.epam.epamgymdemo.repository.TrainerRepository;
import com.epam.epamgymdemo.repository.TrainingRepository;
import com.epam.epamgymdemo.repository.TrainingTypeRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


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

    @Mock
    private CustomMetrics customMetrics;

    @Mock
    private BoToDtoConverter boToDtoConverter;

    @Mock
    private TrainingSummaryDtoConsumer trainingSummaryDtoConsumer;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    void testCreate() {
        User user = User.builder().id(1L).build();

        TrainingType trainingType = TrainingType.builder().id(3L).build();

        when(trainingTypeRepository.findById(3L)).thenReturn(Optional.ofNullable(trainingType));
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        trainerService.create(1L, 3L);

        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    void testUpdate() {
        User user = User.builder()
                .username("username")
                .build();
        Trainer trainer = Trainer.builder()
                .id(1L).user(user)
                .trainingType(TrainingType.builder()
                        .id(1L)
                        .build())
                .trainees(new HashSet<>())
                .build();
        user.setTrainer(trainer);

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        when(boToDtoConverter.trainerToTrainerWithListDto(trainer, new HashSet<>())).thenReturn(
                TrainerWithListDto.builder().specializationId(trainer.getTrainingType().getId()).build()
        );

        TrainerWithListDto trainerWithListDto = trainerService.update("username");

        assertEquals(trainer.getTrainingType().getId(), trainerWithListDto.getSpecializationId());
    }

    @Test
    void testGet() {
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .isActive(true)
                .username("username")
                .build();
        Trainer trainer = Trainer.builder()
                .id(1L)
                .user(user)
                .trainingType(TrainingType.builder()
                        .id(1L)
                        .build())
                .trainees(new HashSet<>())
                .build();
        user.setTrainer(trainer);
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        when(boToDtoConverter.trainerToTrainerWithListDto(trainer, new HashSet<>())).thenReturn(
                TrainerWithListDto.builder().specializationId(trainer.getTrainingType().getId()).build()
        );

        TrainerDto trainerDto = trainerService.get("username");

        assertEquals(trainerDto.getSpecializationId(), trainer.getTrainingType().getId());
    }

    @Test
    void testGetAll() {
        trainerService.getAll();

        verify(trainerRepository).findAll();
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
                        .build())
                .trainingDate(LocalDate.now())
                .trainee(trainee)
                .build();
        userTrainer.setTrainer(trainer);

        when(trainingRepository
                .findTrainings("username", null, null, null))
                .thenReturn(List.of(training));

        List<TrainerTrainingDto> trainings = trainerService.getTrainingsByUsernameAndCriteria(
                "username", null, null, null);

        assertEquals(1, trainings.size());
    }

    @Test
    void testGetByUsernames() {
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .isActive(true)
                .username("username")
                .build();
        Trainer trainer = Trainer.builder()
                .id(1L)
                .user(user)
                .trainingType(TrainingType.builder()
                        .id(1L)
                        .build())
                .trainees(new HashSet<>())
                .build();
        user.setTrainer(trainer);
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        List<Trainer> trainers = trainerService.getByUsernames(List.of("username"));

        assertEquals(trainers.size(), 1);
    }

    @Test
    public void testGetTraineeList() {
        User trainerUser = User.builder().username("trainer1").build();
        Trainer trainer = Trainer.builder().user(trainerUser).build();
        trainerUser.setTrainer(trainer);

        Trainee trainee1 = Trainee.builder()
                .user(User.builder().username("trainee1").firstName("John").lastName("Doe").isActive(true).build())
                .build();

        Trainee trainee2 = Trainee.builder()
                .user(User.builder().username("trainee2").firstName("Jane").lastName("Smith").isActive(true).build())
                .build();

        Set<Trainee> trainees = new HashSet<>();
        trainees.add(trainee1);
        trainees.add(trainee2);

        trainer.setTrainees(trainees);

        when(userRepository.findByUsername("trainer1")).thenReturn(Optional.of(trainerUser));

        Set<UserDto> result = trainerService.getTraineeList("trainer1");

        assertEquals(2, result.size());
        for (UserDto userDto : result) {
            if (userDto.getUsername().equals("trainee1")) {
                assertEquals("John", userDto.getFirstName());
                assertEquals("Doe", userDto.getLastName());
                assertEquals(true, userDto.getIsActive());
            } else if (userDto.getUsername().equals("trainee2")) {
                assertEquals("Jane", userDto.getFirstName());
                assertEquals("Smith", userDto.getLastName());
                assertEquals(true, userDto.getIsActive());
            } else {
                throw new AssertionError("Unexpected userDto: " + userDto.getUsername());
            }
        }
    }
}
