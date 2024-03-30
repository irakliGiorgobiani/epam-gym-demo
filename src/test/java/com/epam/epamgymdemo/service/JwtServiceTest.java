package com.epam.epamgymdemo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    public void setup() {
        jwtService = new JwtService();
    }

    @Test
    public void testGenerateToken() {
        UserDetails userDetails = new User("testUser", "password", Collections.emptyList());
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
    }
}

