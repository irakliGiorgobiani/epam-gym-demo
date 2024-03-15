package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.exception.MissingInstanceException;
import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.model.dto.UsernamePasswordDto;
import com.epam.epamgymdemo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    User user = User.builder()
            .username("username")
            .password("password")
            .build();

    @Test
    void testIsValidUser() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.ofNullable(user));

        boolean isValidUser = authenticationService.isValidUser(user.getUsername(), user.getPassword());

        assertTrue(isValidUser);
    }

    @Test
    void testAuthenticateUserSuccess() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.ofNullable(user));

        assertDoesNotThrow(() -> authenticationService.authenticateUser("username", "password"));
    }

    @Test
    void testAuthenticateUserFailure() {
        assertThrows(MissingInstanceException.class, () -> authenticationService.authenticateUser("exception", "expected"));
    }

    @Test
    void testUsernamePassword(){
        UsernamePasswordDto usernamePasswordDto = authenticationService.usernamePassword("username", "password");

        assertEquals(usernamePasswordDto.getUsername(), "username");
        assertEquals(usernamePasswordDto.getPassword(), "password");
    }
}

