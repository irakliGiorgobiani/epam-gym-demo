package com.epam.epamgymdemo.model.dto;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class TrainerWithListDtoTest {

    @Test
    void testNoArgsConstructor() {
        assertDoesNotThrow(() -> new TrainerWithListDto());
    }

    @Test
    void testAllArgsConstructor() {
        Set<UserDto> trainees = new HashSet<>();
        assertDoesNotThrow(() -> new TrainerWithListDto(trainees));
    }

    @Test
    void testGetSetTrainees() {
        Set<UserDto> trainees = new HashSet<>();
        trainees.add(new UserDto());
        TrainerWithListDto trainerWithListDto = new TrainerWithListDto();
        trainerWithListDto.setTrainees(trainees);
        assertEquals(trainees, trainerWithListDto.getTrainees());
    }

    @Test
    void testBuilder() {
        Set<UserDto> trainees = new HashSet<>();
        trainees.add(new UserDto());
        TrainerWithListDto trainerWithListDto = TrainerWithListDto.builder().specializationId(1L).firstName("First").lastName("Last").username("Username").isActive(true).trainees(trainees).build();
        assertEquals(1L, trainerWithListDto.getSpecializationId());
        assertEquals("First", trainerWithListDto.getFirstName());
        assertEquals("Last", trainerWithListDto.getLastName());
        assertEquals("Username", trainerWithListDto.getUsername());
        assertTrue(trainerWithListDto.getIsActive());
        assertEquals(trainees, trainerWithListDto.getTrainees());
    }

    @Test
    void testEquals() {
        TrainerWithListDto dto1 = TrainerWithListDto.builder()
                .firstName("John")
                .lastName("Doe")
                .username("johndoe")
                .isActive(true)
                .specializationId(1L)
                .build();

        TrainerWithListDto dto2 = TrainerWithListDto.builder()
                .firstName("John")
                .lastName("Doe")
                .username("johndoe")
                .isActive(true)
                .specializationId(1L)
                .build();

        TrainerWithListDto dto3 = TrainerWithListDto.builder()
                .firstName("Jane")
                .lastName("Doe")
                .username("janedoe")
                .isActive(false)
                .specializationId(2L)
                .build();

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testHashCode() {
        TrainerWithListDto dto1 = TrainerWithListDto.builder()
                .firstName("John")
                .lastName("Doe")
                .username("johndoe")
                .isActive(true)
                .specializationId(1L)
                .build();

        TrainerWithListDto dto2 = TrainerWithListDto.builder()
                .firstName("John")
                .lastName("Doe")
                .username("johndoe")
                .isActive(true)
                .specializationId(1L)
                .build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TrainerWithListDto dto = TrainerWithListDto.builder()
                .firstName("John")
                .lastName("Doe")
                .username("johndoe")
                .isActive(true)
                .specializationId(1L)
                .build();

        assertNotNull(dto.toString());
    }

    @Test
    void testCanEqual() {
        TrainerWithListDto dto1 = TrainerWithListDto.builder()
                .firstName("John")
                .lastName("Doe")
                .username("johndoe")
                .isActive(true)
                .specializationId(1L)
                .build();

        TrainerWithListDto dto2 = TrainerWithListDto.builder()
                .firstName("John")
                .lastName("Doe")
                .username("johndoe")
                .isActive(true)
                .specializationId(1L)
                .build();

        assertTrue(dto1.canEqual(dto2));
    }
}
