package com.epam.epamgymdemo.model.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class TrainerDtoTest {

    @Test
    void testNoArgsConstructor() {
        assertDoesNotThrow(() -> new TrainerDto());
    }

    @Test
    void testAllArgsConstructor() {
        assertDoesNotThrow(() -> new TrainerDto(1L));
    }

    @Test
    void testGetSetSpecializationId() {
        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setSpecializationId(1L);
        assertEquals(1L, trainerDto.getSpecializationId());
    }

    @Test
    void testBuilder() {
        TrainerDto trainerDto = TrainerDto.builder().firstName("John").lastName("Doe").username("username").isActive(true).specializationId(1L).build();
        assertEquals("John", trainerDto.getFirstName());
        assertEquals("Doe", trainerDto.getLastName());
        assertEquals("username", trainerDto.getUsername());
        assertTrue(trainerDto.getIsActive());
        assertEquals(1L, trainerDto.getSpecializationId());
    }
}

