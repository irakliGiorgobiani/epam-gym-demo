package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.exception.BruteForceException;
import com.epam.epamgymdemo.exception.EntityNotFoundException;
import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.security.auth.login.CredentialNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private HttpServletRequest httpRequest;

    @Mock
    private HttpSession session;

    @Test
    void testCheckCredentials_WhenMaxAttemptsExceeded() {
        String username = "testuser";
        String password = "testpassword";

        User user = new User();
        user.setUsername(username);
        user.setPassword("encodedPassword");

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        Map<String, Integer> attempts = new HashMap<>();
        attempts.put(session.getId(), AuthenticationService.MAX_ATTEMPT - 1);

        Mockito.when(httpRequest.getSession(true)).thenReturn(session);
        Mockito.when(session.getAttribute("attempts")).thenReturn(attempts);

        assertThrows(BruteForceException.class, () -> authenticationService.checkCredentials(username, password));
    }

    @Test
    void testCheckCredentials_WhenSuccessful() throws CredentialNotFoundException {
        String username = "testuser";
        String password = "testpassword";

        User user = new User();
        user.setUsername(username);
        user.setPassword("encodedPassword");

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        Map<String, Integer> attempts = new HashMap<>();
        attempts.put(session.getId(), AuthenticationService.MAX_ATTEMPT - 1);

        Mockito.when(httpRequest.getSession(true)).thenReturn(session);
        Mockito.when(session.getAttribute("attempts")).thenReturn(attempts);

        authenticationService.checkCredentials(username, password);
    }
}
