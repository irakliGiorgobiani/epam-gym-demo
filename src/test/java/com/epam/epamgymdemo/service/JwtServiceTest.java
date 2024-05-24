package com.epam.epamgymdemo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;

    private UserDetails userDetails;
    private String token;

    @BeforeEach
    public void setUp() {
        jwtService = new JwtService();

        userDetails = new User("username", "password", Collections.emptyList());

        token = jwtService.generateToken(userDetails);
    }

    @Test
    public void testGenerateToken() {
        assertNotNull(token);
    }

    @Test
    void testTokenBlacklisting() {
        userDetails = User.withUsername("username").password("password").roles("USER").build();
        String token = jwtService.generateToken(userDetails);

        assertFalse(jwtService.isTokenBlacklisted(token));

        jwtService.addToTokenBlacklist(token);

        assertTrue(jwtService.isTokenBlacklisted(token));
    }

    @Test
    public void testExtractUsernameFromToken() {
        String usernameFromToken = jwtService.extractUsername(token);

        assertEquals(userDetails.getUsername(), usernameFromToken);
    }

    @Test
    public void testValidateNonExpiredToken() {
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }
}

