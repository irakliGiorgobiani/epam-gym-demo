package com.epam.epamgymdemo.model.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ActiveDtoTest {

    @Test
    void testNoArgsConstructor() {
        assertDoesNotThrow(() -> new ActiveDto());
    }

    @Test
    void testAllArgsConstructor() {
        assertDoesNotThrow(() -> new ActiveDto(true));
    }

    @Test
    void testGetSetIsActive() {
        ActiveDto activeDto = new ActiveDto();
        activeDto.setIsActive(true);
        assertTrue(activeDto.getIsActive());
    }

    @Test
    void testBuilder() {
        ActiveDto activeDto = ActiveDto.builder().isActive(true).build();
        assertTrue(activeDto.getIsActive());
    }

    @Test
    void testEquals() {
        ActiveDto dto1 = ActiveDto.builder()
                .isActive(true)
                .build();

        ActiveDto dto2 = ActiveDto.builder()
                .isActive(true)
                .build();

        ActiveDto dto3 = ActiveDto.builder()
                .isActive(false)
                .build();

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testHashCode() {
        ActiveDto dto1 = ActiveDto.builder()
                .isActive(true)
                .build();

        ActiveDto dto2 = ActiveDto.builder()
                .isActive(true)
                .build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ActiveDto dto = ActiveDto.builder()
                .isActive(true)
                .build();

        assertNotNull(dto.toString());
    }

    @Test
    void testCanEqual() {
        ActiveDto dto1 = ActiveDto.builder()
                .isActive(true)
                .build();

        ActiveDto dto2 = ActiveDto.builder()
                .isActive(true)
                .build();

        assertTrue(dto1.canEqual(dto2));
    }
}

