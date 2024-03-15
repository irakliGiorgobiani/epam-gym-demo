package com.epam.epamgymdemo.model.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class TrainingDtoTest {

    @Test
    void testNoArgsConstructor() {
        assertDoesNotThrow(() -> new TrainingDto());
    }

    @Test
    void testAllArgsConstructor() {
        assertDoesNotThrow(() -> new TrainingDto("traineeUsername", "trainerUsername", "trainingName", LocalDate.now(), 1));
    }

    @Test
    void testGetSetTraineeUsername() {
        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setTraineeUsername("traineeUsername");
        assertEquals("traineeUsername", trainingDto.getTraineeUsername());
    }

    @Test
    void testGetSetTrainerUsername() {
        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setTrainerUsername("trainerUsername");
        assertEquals("trainerUsername", trainingDto.getTrainerUsername());
    }

    @Test
    void testGetSetTrainingName() {
        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setTrainingName("trainingName");
        assertEquals("trainingName", trainingDto.getTrainingName());
    }

    @Test
    void testGetSetTrainingDate() {
        LocalDate date = LocalDate.now();
        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setTrainingDate(date);
        assertEquals(date, trainingDto.getTrainingDate());
    }

    @Test
    void testGetSetTrainingDuration() {
        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setTrainingDuration(1);
        assertEquals(1, trainingDto.getTrainingDuration());
    }

    @Test
    void testBuilder() {
        LocalDate date = LocalDate.now();
        TrainingDto trainingDto = TrainingDto.builder().traineeUsername("traineeUsername").trainerUsername("trainerUsername").trainingName("trainingName").trainingDate(date).trainingDuration(1).build();
        assertEquals("traineeUsername", trainingDto.getTraineeUsername());
        assertEquals("trainerUsername", trainingDto.getTrainerUsername());
        assertEquals("trainingName", trainingDto.getTrainingName());
        assertEquals(date, trainingDto.getTrainingDate());
        assertEquals(1, trainingDto.getTrainingDuration());
    }

    @Test
    void testEquals() {
        TrainingDto dto1 = TrainingDto.builder()
                .traineeUsername("trainee1")
                .trainerUsername("trainer1")
                .trainingName("training1")
                .trainingDate(LocalDate.of(2024, 3, 15))
                .trainingDuration(1)
                .build();

        TrainingDto dto2 = TrainingDto.builder()
                .traineeUsername("trainee1")
                .trainerUsername("trainer1")
                .trainingName("training1")
                .trainingDate(LocalDate.of(2024, 3, 15))
                .trainingDuration(1)
                .build();

        TrainingDto dto3 = TrainingDto.builder()
                .traineeUsername("trainee2")
                .trainerUsername("trainer2")
                .trainingName("training2")
                .trainingDate(LocalDate.of(2024, 3, 16))
                .trainingDuration(2)
                .build();

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testHashCode() {
        TrainingDto dto1 = TrainingDto.builder()
                .traineeUsername("trainee1")
                .trainerUsername("trainer1")
                .trainingName("training1")
                .trainingDate(LocalDate.of(2024, 3, 15))
                .trainingDuration(1)
                .build();

        TrainingDto dto2 = TrainingDto.builder()
                .traineeUsername("trainee1")
                .trainerUsername("trainer1")
                .trainingName("training1")
                .trainingDate(LocalDate.of(2024, 3, 15))
                .trainingDuration(1)
                .build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TrainingDto dto = TrainingDto.builder()
                .traineeUsername("trainee1")
                .trainerUsername("trainer1")
                .trainingName("training1")
                .trainingDate(LocalDate.of(2024, 3, 15))
                .trainingDuration(1)
                .build();

        assertNotNull(dto.toString());
    }

    @Test
    void testCanEqual() {
        TrainingDto dto1 = TrainingDto.builder()
                .traineeUsername("trainee1")
                .trainerUsername("trainer1")
                .trainingName("training1")
                .trainingDate(LocalDate.of(2024, 3, 15))
                .trainingDuration(1)
                .build();

        TrainingDto dto2 = TrainingDto.builder()
                .traineeUsername("trainee1")
                .trainerUsername("trainer1")
                .trainingName("training1")
                .trainingDate(LocalDate.of(2024, 3, 15))
                .trainingDuration(1)
                .build();

        assertTrue(dto1.canEqual(dto2));
    }
}
