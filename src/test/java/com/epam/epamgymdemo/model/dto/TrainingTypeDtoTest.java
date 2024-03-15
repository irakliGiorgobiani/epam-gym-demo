package com.epam.epamgymdemo.model.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainingTypeDtoTest {

    @Test
    void testNoArgsConstructor() {
        assertDoesNotThrow(() -> new TrainingTypeDto());
    }

    @Test
    void testAllArgsConstructor() {
        assertDoesNotThrow(() -> new TrainingTypeDto("TypeName", 1L));
    }

    @Test
    void testBuilder() {
        TrainingTypeDto trainingTypeDto = TrainingTypeDto.builder()
                .typeName("TypeName")
                .id(1L)
                .build();

        assertEquals("TypeName", trainingTypeDto.getTypeName());
        assertEquals(1L, trainingTypeDto.getId());
    }

    @Test
    void testEquals() {
        TrainingTypeDto dto1 = TrainingTypeDto.builder()
                .typeName("type1")
                .id(1L)
                .build();

        TrainingTypeDto dto2 = TrainingTypeDto.builder()
                .typeName("type1")
                .id(1L)
                .build();

        TrainingTypeDto dto3 = TrainingTypeDto.builder()
                .typeName("type2")
                .id(2L)
                .build();

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testHashCode() {
        TrainingTypeDto dto1 = TrainingTypeDto.builder()
                .typeName("type1")
                .id(1L)
                .build();

        TrainingTypeDto dto2 = TrainingTypeDto.builder()
                .typeName("type1")
                .id(1L)
                .build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TrainingTypeDto dto = TrainingTypeDto.builder()
                .typeName("type1")
                .id(1L)
                .build();

        assertNotNull(dto.toString());
    }

    @Test
    void testCanEqual() {
        TrainingTypeDto dto1 = TrainingTypeDto.builder()
                .typeName("type1")
                .id(1L)
                .build();

        TrainingTypeDto dto2 = TrainingTypeDto.builder()
                .typeName("type1")
                .id(1L)
                .build();

        assertTrue(dto1.canEqual(dto2));
    }
}

