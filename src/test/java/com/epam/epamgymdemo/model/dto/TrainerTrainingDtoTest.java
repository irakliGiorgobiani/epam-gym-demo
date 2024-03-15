package com.epam.epamgymdemo.model.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class TrainerTrainingDtoTest {

    @Test
    void testNoArgsConstructor() {
        assertDoesNotThrow(() -> new TrainerTrainingDto());
    }

    @Test
    void testAllArgsConstructor() {
        assertDoesNotThrow(() -> new TrainerTrainingDto("Training", LocalDate.now(), "Type", 1, "Trainee"));
    }

    @Test
    void testGetSetTrainingName() {
        TrainerTrainingDto trainerTrainingDto = new TrainerTrainingDto();
        trainerTrainingDto.setTrainingName("Training");
        assertEquals("Training", trainerTrainingDto.getTrainingName());
    }

    @Test
    void testGetSetTrainingDate() {
        LocalDate date = LocalDate.now();
        TrainerTrainingDto trainerTrainingDto = new TrainerTrainingDto();
        trainerTrainingDto.setTrainingDate(date);
        assertEquals(date, trainerTrainingDto.getTrainingDate());
    }

    @Test
    void testGetSetTrainingType() {
        TrainerTrainingDto trainerTrainingDto = new TrainerTrainingDto();
        trainerTrainingDto.setTrainingType("Type");
        assertEquals("Type", trainerTrainingDto.getTrainingType());
    }

    @Test
    void testGetSetTrainingDuration() {
        TrainerTrainingDto trainerTrainingDto = new TrainerTrainingDto();
        trainerTrainingDto.setTrainingDuration(1);
        assertEquals(1, trainerTrainingDto.getTrainingDuration());
    }

    @Test
    void testGetSetTraineeName() {
        TrainerTrainingDto trainerTrainingDto = new TrainerTrainingDto();
        trainerTrainingDto.setTraineeName("Trainee");
        assertEquals("Trainee", trainerTrainingDto.getTraineeName());
    }

    @Test
    void testBuilder() {
        TrainerTrainingDto trainerTrainingDto = TrainerTrainingDto.builder().trainingName("Training").trainingDate(LocalDate.now()).trainingType("Type").trainingDuration(1).traineeName("Trainee").build();
        assertEquals("Training", trainerTrainingDto.getTrainingName());
        assertEquals(LocalDate.now(), trainerTrainingDto.getTrainingDate());
        assertEquals("Type", trainerTrainingDto.getTrainingType());
        assertEquals(1, trainerTrainingDto.getTrainingDuration());
        assertEquals("Trainee", trainerTrainingDto.getTraineeName());
    }

    @Test
    void testEquals() {
        TrainerTrainingDto dto1 = TrainerTrainingDto.builder()
                .trainingName("training1")
                .trainingDate(LocalDate.of(2024, 3, 15))
                .trainingType("type1")
                .trainingDuration(1)
                .traineeName("trainee1")
                .build();

        TrainerTrainingDto dto2 = TrainerTrainingDto.builder()
                .trainingName("training1")
                .trainingDate(LocalDate.of(2024, 3, 15))
                .trainingType("type1")
                .trainingDuration(1)
                .traineeName("trainee1")
                .build();

        TrainerTrainingDto dto3 = TrainerTrainingDto.builder()
                .trainingName("training2")
                .trainingDate(LocalDate.of(2024, 3, 16))
                .trainingType("type2")
                .trainingDuration(2)
                .traineeName("trainee2")
                .build();

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testHashCode() {
        TrainerTrainingDto dto1 = TrainerTrainingDto.builder()
                .trainingName("training1")
                .trainingDate(LocalDate.of(2024, 3, 15))
                .trainingType("type1")
                .trainingDuration(1)
                .traineeName("trainee1")
                .build();

        TrainerTrainingDto dto2 = TrainerTrainingDto.builder()
                .trainingName("training1")
                .trainingDate(LocalDate.of(2024, 3, 15))
                .trainingType("type1")
                .trainingDuration(1)
                .traineeName("trainee1")
                .build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TrainerTrainingDto dto = TrainerTrainingDto.builder()
                .trainingName("training1")
                .trainingDate(LocalDate.of(2024, 3, 15))
                .trainingType("type1")
                .trainingDuration(1)
                .traineeName("trainee1")
                .build();

        assertNotNull(dto.toString());
    }

    @Test
    void testCanEqual() {
        TrainerTrainingDto dto1 = TrainerTrainingDto.builder()
                .trainingName("training1")
                .trainingDate(LocalDate.of(2024, 3, 15))
                .trainingType("type1")
                .trainingDuration(1)
                .traineeName("trainee1")
                .build();

        TrainerTrainingDto dto2 = TrainerTrainingDto.builder()
                .trainingName("training1")
                .trainingDate(LocalDate.of(2024, 3, 15))
                .trainingType("type1")
                .trainingDuration(1)
                .traineeName("trainee1")
                .build();

        assertTrue(dto1.canEqual(dto2));
    }
}

