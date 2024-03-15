package com.epam.epamgymdemo.model.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsernamePasswordDtoTest {

    @Test
    void testNoArgsConstructor() {
        assertDoesNotThrow(() -> new UsernamePasswordDto());
    }

    @Test
    void testAllArgsConstructor() {
        assertDoesNotThrow(() -> new UsernamePasswordDto("username", "password"));
    }

    @Test
    void testBuilder() {
        UsernamePasswordDto usernamePasswordDto = UsernamePasswordDto.builder()
                .username("username")
                .password("password")
                .build();

        assertEquals("username", usernamePasswordDto.getUsername());
        assertEquals("password", usernamePasswordDto.getPassword());
    }

    @Test
    void testEquals() {
        UsernamePasswordDto dto1 = UsernamePasswordDto.builder().username("user").password("pass").build();
        UsernamePasswordDto dto2 = UsernamePasswordDto.builder().username("user").password("pass").build();
        UsernamePasswordDto dto3 = UsernamePasswordDto.builder().username("user2").password("pass").build();

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testHashCode() {
        UsernamePasswordDto dto1 = UsernamePasswordDto.builder().username("user").password("pass").build();
        UsernamePasswordDto dto2 = UsernamePasswordDto.builder().username("user").password("pass").build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        UsernamePasswordDto dto = UsernamePasswordDto.builder().username("user").password("pass").build();
        assertNotNull(dto.toString());
    }

    @Test
    void testSetUsername() {
        UsernamePasswordDto dto = UsernamePasswordDto.builder().build();
        dto.setUsername("user");
        assertEquals("user", dto.getUsername());
    }

    @Test
    void testSetPassword() {
        UsernamePasswordDto dto = UsernamePasswordDto.builder().build();
        dto.setPassword("pass");
        assertEquals("pass", dto.getPassword());
    }

    @Test
    void testCanEqual() {
        UsernamePasswordDto dto1 = UsernamePasswordDto.builder().username("user").password("pass").build();
        UsernamePasswordDto dto2 = UsernamePasswordDto.builder().username("user").password("pass").build();
        assertTrue(dto1.canEqual(dto2));
    }
}

