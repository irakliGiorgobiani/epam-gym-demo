package com.epam.epamgymdemo.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.security.auth.login.CredentialNotFoundException;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestExceptionHandlerTest {

    @Test
    void testHandleCredentialNotFoundException() {
        CredentialNotFoundException exception = new CredentialNotFoundException("Credential not found");
        RestExceptionHandler handler = new RestExceptionHandler();
        ResponseEntity<ErrorResponse> responseEntity = handler.handleCredentialNotFoundException(exception);

        ErrorResponse expected = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message("Credential not found")
                .timestamp(LocalDateTime.now())
                .build();

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals(expected, responseEntity.getBody());
    }

    @Test
    void testHandleMissingFieldException() {
        MissingFieldException exception = new MissingFieldException("Missing field");
        RestExceptionHandler handler = new RestExceptionHandler();
        ResponseEntity<ErrorResponse> responseEntity = handler.handleMissingFieldException(exception);

        ErrorResponse expected = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Missing field")
                .timestamp(Objects.requireNonNull(responseEntity.getBody()).getTimestamp())
                .build();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(expected, responseEntity.getBody());
    }

    @Test
    void testHandleMissingInstanceException() {
        MissingInstanceException exception = new MissingInstanceException("Missing instance");
        RestExceptionHandler handler = new RestExceptionHandler();
        ResponseEntity<ErrorResponse> responseEntity = handler.handleMissingInstanceException(exception);

        ErrorResponse expected = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message("Missing instance")
                .timestamp(Objects.requireNonNull(responseEntity.getBody()).getTimestamp())
                .build();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(expected, responseEntity.getBody());
    }

    @Test
    void testHandleNamingException() {
        NamingException exception = new NamingException("Naming exception");
        RestExceptionHandler handler = new RestExceptionHandler();
        ResponseEntity<ErrorResponse> responseEntity = handler.handleNamingException(exception);

        ErrorResponse expected = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Naming exception")
                .timestamp(LocalDateTime.now())
                .build();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(expected, responseEntity.getBody());
    }
}

