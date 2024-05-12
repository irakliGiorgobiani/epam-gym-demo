package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.exception.BruteForceException;
import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.security.auth.login.CredentialNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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

    private String username;

    private String password;

    private User user;

    @Value("${MAX_ATTEMPT}")
    private int MAX_ATTEMPT;

    @BeforeEach
    public void setUp() {
        username = "testuser";
        password = "testpassword";

        user = new User();
        user.setUsername(username);
        user.setPassword("encodedPassword");
    }

    @Test
    void testCheckCredentials_WhenMaxAttemptsExceeded() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        Map<String, Integer> attempts = new HashMap<>();
        attempts.put(session.getId(), MAX_ATTEMPT - 1);

        when(httpRequest.getSession(true)).thenReturn(session);
        when(session.getAttribute("attempts")).thenReturn(attempts);

        assertThrows(BruteForceException.class, () -> authenticationService.checkCredentials(username, password));
    }

    @Test
    void testCheckCredentials_WhenSuccessful() throws CredentialNotFoundException {
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        Map<String, Integer> attempts = new HashMap<>();
        attempts.put(session.getId(), MAX_ATTEMPT - 1);

        when(httpRequest.getSession(true)).thenReturn(session);
        when(session.getAttribute("attempts")).thenReturn(attempts);

        authenticationService.checkCredentials(username, password);
    }
}
