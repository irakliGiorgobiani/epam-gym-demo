package com.epam.epamgymdemo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;

    private final String username = "testUser";

    private final String password = "password";

    @BeforeEach
    public void setUp() {
        jwtService = new JwtService();
    }

    @Test
    public void testGenerateToken() {
        UserDetails userDetails = new User(username, password, Collections.emptyList());

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
    }

    @Test
    void testTokenBlacklisting() {
        UserDetails userDetails = User.withUsername(username).password(password).roles("USER").build();
        String token = jwtService.generateToken(userDetails);

        assertFalse(jwtService.isTokenBlacklisted(token));

        jwtService.addToTokenBlacklist(token);

        assertTrue(jwtService.isTokenBlacklisted(token));
    }
}

