package com.epam.epamgymdemo.exception;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ErrorResponseTest {

    @Test
    void testNoArgsConstructor() {
        assertDoesNotThrow(() -> new ErrorResponse());
    }

    @Test
    void testAllArgsConstructor() {
        assertDoesNotThrow(() -> new ErrorResponse(404, "Not Found", "Resource not found", LocalDateTime.now()));
    }

    @Test
    void testBuilder() {
        LocalDateTime timestamp = LocalDateTime.now();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(404)
                .error("Not Found")
                .message("Resource not found")
                .timestamp(timestamp)
                .build();

        assertEquals(404, errorResponse.getStatus());
        assertEquals("Not Found", errorResponse.getError());
        assertEquals("Resource not found", errorResponse.getMessage());
        assertEquals(timestamp, errorResponse.getTimestamp());
    }

    @Test
    void testEquals() {
        ErrorResponse errorResponse1 = ErrorResponse.builder()
                .status(404)
                .error("Not Found")
                .message("Resource not found")
                .timestamp(LocalDateTime.now())
                .build();

        ErrorResponse errorResponse2 = ErrorResponse.builder()
                .status(404)
                .error("Not Found")
                .message("Resource not found")
                .timestamp(LocalDateTime.now())
                .build();

        ErrorResponse errorResponse3 = ErrorResponse.builder()
                .status(500)
                .error("Internal Server Error")
                .message("Something went wrong")
                .timestamp(LocalDateTime.now())
                .build();

        assertEquals(errorResponse1, errorResponse2);
        assertNotEquals(errorResponse1, errorResponse3);
    }

    @Test
    void testHashCode() {
        ErrorResponse errorResponse1 = ErrorResponse.builder()
                .status(404)
                .error("Not Found")
                .message("Resource not found")
                .timestamp(LocalDateTime.now())
                .build();

        ErrorResponse errorResponse2 = ErrorResponse.builder()
                .status(404)
                .error("Not Found")
                .message("Resource not found")
                .timestamp(LocalDateTime.now())
                .build();

        assertEquals(errorResponse1.hashCode(), errorResponse2.hashCode());
    }

    @Test
    void testToString() {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(404)
                .error("Not Found")
                .message("Resource not found")
                .timestamp(LocalDateTime.now())
                .build();

        assertNotNull(errorResponse.toString());
    }

    @Test
    void testSetStatus() {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(404)
                .error("Not Found")
                .message("Resource not found")
                .timestamp(LocalDateTime.now())
                .build();

        errorResponse.setStatus(500);

        assertEquals(500, errorResponse.getStatus());
    }

    @Test
    void testSetError() {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(404)
                .error("Not Found")
                .message("Resource not found")
                .timestamp(LocalDateTime.now())
                .build();

        errorResponse.setError("Internal Server Error");

        assertEquals("Internal Server Error", errorResponse.getError());
    }

    @Test
    void testSetMessage() {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(404)
                .error("Not Found")
                .message("Resource not found")
                .timestamp(LocalDateTime.now())
                .build();

        errorResponse.setMessage("Resource not found, please check your request");

        assertEquals("Resource not found, please check your request", errorResponse.getMessage());
    }

    @Test
    void testSetTimestamp() {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(404)
                .error("Not Found")
                .message("Resource not found")
                .timestamp(LocalDateTime.now())
                .build();

        LocalDateTime newTimestamp = LocalDateTime.now().minusDays(1);
        errorResponse.setTimestamp(newTimestamp);

        assertEquals(newTimestamp, errorResponse.getTimestamp());
    }
}

