package com.epam.epamgymdemo;

import com.epam.epamgymdemo.model.User;
import com.epam.epamgymdemo.service.AuthenticationService;
import com.epam.epamgymdemo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.management.InstanceNotFoundException;
import javax.security.auth.login.CredentialNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest {

    private AuthenticationService authenticationService;
    @Mock
    private UserService userService;
    String username = "username";
    String password = "password";
    User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(userService);

        user = User.builder()
                .username(username)
                .password(password)
                .build();
    }

    @Test
    void testIsValid() throws InstanceNotFoundException {
        when(userService.getByUsername(username)).thenReturn(user);

        boolean isValid = authenticationService.isValidUser(user.getUsername(), user.getPassword());

        assertTrue(isValid);
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
    void testAuthenticateUserSuccess() throws InstanceNotFoundException {
        when(userService.getByUsername(username)).thenReturn(user);

        String token = authenticationService.authenticateUser(username, password);

        assertNotNull(token);
    }

    @Test
    void testAuthenticateUserFailure() throws InstanceNotFoundException {
        String token = authenticationService.authenticateUser(username, password);

        assertNull(token);
    }
}