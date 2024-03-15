package com.epam.epamgymdemo.model.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class TraineeTrainingDtoTest {

    @Test
    void testNoArgsConstructor() {
        assertDoesNotThrow(() -> new TraineeTrainingDto());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDate date = LocalDate.now();
        assertDoesNotThrow(() -> new TraineeTrainingDto("Training", date, "Type", 1, "Trainer"));
    }

    @Test
    void testGetSetTrainingName() {
        TraineeTrainingDto traineeTrainingDto = new TraineeTrainingDto();
        traineeTrainingDto.setTrainingName("Training");
        assertEquals("Training", traineeTrainingDto.getTrainingName());
    }

    @Test
    void testGetSetTrainingDate() {
        LocalDate date = LocalDate.now();
        TraineeTrainingDto traineeTrainingDto = new TraineeTrainingDto();
        traineeTrainingDto.setTrainingDate(date);
        assertEquals(date, traineeTrainingDto.getTrainingDate());
    }

    @Test
    void testGetSetTrainingType() {
        TraineeTrainingDto traineeTrainingDto = new TraineeTrainingDto();
        traineeTrainingDto.setTrainingType("Type");
        assertEquals("Type", traineeTrainingDto.getTrainingType());
    }

    @Test
    void testGetSetTrainingDuration() {
        TraineeTrainingDto traineeTrainingDto = new TraineeTrainingDto();
        traineeTrainingDto.setTrainingDuration(1);
        assertEquals(1, traineeTrainingDto.getTrainingDuration());
    }

    @Test
    void testGetSetTrainerName() {
        TraineeTrainingDto traineeTrainingDto = new TraineeTrainingDto();
        traineeTrainingDto.setTrainerName("Trainer");
        assertEquals("Trainer", traineeTrainingDto.getTrainerName());
    }

    @Test
    void testBuilder() {
        LocalDate date = LocalDate.now();
        TraineeTrainingDto traineeTrainingDto = TraineeTrainingDto.builder().trainingName("Training").trainingDate(date).trainingType("Type").trainingDuration(1).trainerName("Trainer").build();
        assertEquals("Training", traineeTrainingDto.getTrainingName());
        assertEquals(date, traineeTrainingDto.getTrainingDate());
        assertEquals("Type", traineeTrainingDto.getTrainingType());
        assertEquals(1, traineeTrainingDto.getTrainingDuration());
        assertEquals("Trainer", traineeTrainingDto.getTrainerName());
    }

    @Test
    void testEquals() {
        TraineeTrainingDto dto1 = TraineeTrainingDto.builder()
                .trainingName("training1")
                .trainingDate(LocalDate.of(2024, 3, 15))
                .trainingType("type1")
                .trainingDuration(1)
                .trainerName("trainer1")
                .build();

        TraineeTrainingDto dto2 = TraineeTrainingDto.builder()
                .trainingName("training1")
                .trainingDate(LocalDate.of(2024, 3, 15))
                .trainingType("type1")
                .trainingDuration(1)
                .trainerName("trainer1")
                .build();

        TraineeTrainingDto dto3 = TraineeTrainingDto.builder()
                .trainingName("training2")
                .trainingDate(LocalDate.of(2024, 3, 16))
                .trainingType("type2")
                .trainingDuration(2)
                .trainerName("trainer2")
                .build();

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testHashCode() {
        TraineeTrainingDto dto1 = TraineeTrainingDto.builder()
                .trainingName("training1")
                .trainingDate(LocalDate.of(2024, 3, 15))
                .trainingType("type1")
                .trainingDuration(1)
                .trainerName("trainer1")
                .build();

        TraineeTrainingDto dto2 = TraineeTrainingDto.builder()
                .trainingName("training1")
                .trainingDate(LocalDate.of(2024, 3, 15))
                .trainingType("type1")
                .trainingDuration(1)
                .trainerName("trainer1")
                .build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TraineeTrainingDto dto = TraineeTrainingDto.builder()
                .trainingName("training1")
                .trainingDate(LocalDate.of(2024, 3, 15))
                .trainingType("type1")
                .trainingDuration(1)
                .trainerName("trainer1")
                .build();

        assertNotNull(dto.toString());
    }

    @Test
    void testCanEqual() {
        TraineeTrainingDto dto1 = TraineeTrainingDto.builder()
                .trainingName("training1")
                .trainingDate(LocalDate.of(2024, 3, 15))
                .trainingType("type1")
                .trainingDuration(1)
                .trainerName("trainer1")
                .build();

        TraineeTrainingDto dto2 = TraineeTrainingDto.builder()
                .trainingName("training1")
                .trainingDate(LocalDate.of(2024, 3, 15))
                .trainingType("type1")
                .trainingDuration(1)
                .trainerName("trainer1")
                .build();

        assertTrue(dto1.canEqual(dto2));
    }
}

