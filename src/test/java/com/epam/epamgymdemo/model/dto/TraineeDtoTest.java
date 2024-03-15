package com.epam.epamgymdemo.model.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class TraineeDtoTest {

    @Test
    void testNoArgsConstructor() {
        assertDoesNotThrow(() -> new TraineeDto());
    }

    @Test
    void testAllArgsConstructor() {
        Set<TrainerDto> trainers = new HashSet<>();
        trainers.add(new TrainerDto());
        assertDoesNotThrow(() -> new TraineeDto(LocalDate.now(), "Address", trainers));
    }

    @Test
    void testGetSetBirthday() {
        LocalDate birthday = LocalDate.now();
        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setBirthday(birthday);
        assertEquals(birthday, traineeDto.getBirthday());
    }

    @Test
    void testGetSetAddress() {
        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setAddress("Address");
        assertEquals("Address", traineeDto.getAddress());
    }

    @Test
    void testGetSetTrainers() {
        Set<TrainerDto> trainers = new HashSet<>();
        trainers.add(new TrainerDto());
        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setTrainers(trainers);
        assertEquals(trainers, traineeDto.getTrainers());
    }

    @Test
    void testEqualsAndHashCode() {
        Set<TrainerDto> trainers1 = new HashSet<>();
        trainers1.add(new TrainerDto());
        Set<TrainerDto> trainers2 = new HashSet<>();
        trainers2.add(new TrainerDto());

        TraineeDto traineeDto1 = TraineeDto.builder().firstName("John").lastName("Doe").username("johndoe").isActive(true).birthday(LocalDate.now()).address("Address").trainers(trainers1).build();
        TraineeDto traineeDto2 = TraineeDto.builder().firstName("John").lastName("Doe").username("johndoe").isActive(true).birthday(LocalDate.now()).address("Address").trainers(trainers2).build();

        assertEquals(traineeDto1, traineeDto2);
        assertEquals(traineeDto1.hashCode(), traineeDto2.hashCode());
    }

    @Test
    void testBuilder() {
        Set<TrainerDto> trainers = new HashSet<>();
        trainers.add(new TrainerDto());

        TraineeDto traineeDto = TraineeDto.builder().firstName("John").lastName("Doe").username("johndoe").isActive(true).birthday(LocalDate.now()).address("Address").trainers(trainers).build();
        assertEquals("John", traineeDto.getFirstName());
        assertEquals("Doe", traineeDto.getLastName());
        assertEquals("johndoe", traineeDto.getUsername());
        assertTrue(traineeDto.getIsActive());
        assertEquals(LocalDate.now(), traineeDto.getBirthday());
        assertEquals("Address", traineeDto.getAddress());
        assertEquals(trainers, traineeDto.getTrainers());
    }
}

