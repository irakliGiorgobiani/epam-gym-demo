package com.epam.epamgymdemo.srvice;

import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.repository.UserRepository;
import com.epam.epamgymdemo.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.security.auth.login.CredentialNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
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
        when(userRepository.findByUsername("username")).thenReturn(user);

        boolean isValidUser = authenticationService.isValidUser(user.getUsername(), user.getPassword());

        assertTrue(isValidUser);
    }

    @Test
    void testIsAuthorizedSuccess() {
        authenticationService.getSessions().put("token", "username");

        assertDoesNotThrow(() -> authenticationService.isAuthorized("token"));
    }

    @Test
    void testIsAuthorizedFailure() {
        assertThrows(CredentialNotFoundException.class, () -> authenticationService.isAuthorized("failure"));
    }

    @Test
    void testAuthenticateUserSuccess() {
        when(userRepository.findByUsername("username")).thenReturn(user);

        String token = authenticationService.authenticateUser("username", "password");

        assertNotNull(token);
    }

    @Test
    void testAuthenticateUserFailure() {
        String token = authenticationService.authenticateUser("exception", "expected");

        assertNull(token);
    }
}

