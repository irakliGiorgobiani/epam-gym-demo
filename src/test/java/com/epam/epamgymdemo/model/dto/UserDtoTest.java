package com.epam.epamgymdemo.model.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class UserDtoTest {

    @Test
    void testNoArgsConstructor() {
        assertDoesNotThrow(() -> new UserDto());
    }

    @Test
    void testAllArgsConstructor() {
        assertDoesNotThrow(() -> new UserDto("John", "Doe", "johndoe", true));
    }

    @Test
    void testGetSetFirstName() {
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        assertEquals("John", userDto.getFirstName());
    }

    @Test
    void testGetSetLastName() {
        UserDto userDto = new UserDto();
        userDto.setLastName("Doe");
        assertEquals("Doe", userDto.getLastName());
    }

    @Test
    void testGetSetUsername() {
        UserDto userDto = new UserDto();
        userDto.setUsername("johndoe");
        assertEquals("johndoe", userDto.getUsername());
    }

    @Test
    void testGetSetIsActive() {
        UserDto userDto = new UserDto();
        userDto.setIsActive(true);
        assertTrue(userDto.getIsActive());
    }

    @Test
    void testBuilder() {
        UserDto userDto = UserDto.builder().firstName("John").lastName("Doe").username("johndoe").isActive(true).build();
        assertEquals("John", userDto.getFirstName());
        assertEquals("Doe", userDto.getLastName());
        assertEquals("johndoe", userDto.getUsername());
        assertTrue(userDto.getIsActive());
    }
}

