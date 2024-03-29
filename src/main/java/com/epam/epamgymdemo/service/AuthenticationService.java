package com.epam.epamgymdemo.service;

import lombok.AllArgsConstructor;
import lombok.Generated;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Generated
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    public void authenticateUser(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );
    }
}