package com.epam.epamgymdemo.service;

import lombok.Getter;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import javax.security.auth.login.CredentialNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Getter
public class AuthenticationService {

    private final UserService userService;
    private final Map<String, String> sessions = new HashMap<>();

    public AuthenticationService(UserService userService) {
        this.userService = userService;
    }

    public String authenticateUser(String username, String password) throws InstanceNotFoundException {
        if (isValidUser(username, password)) {
            String token = generateToken();
            sessions.put(token, username);
            return token;
        }
        return null;
    }

    public boolean isValidUser(String username, String password) throws InstanceNotFoundException {
        var user = userService.getByUsername(username);

        return user != null && user.getPassword().equals(password);
    }

    public void isAuthorized(String token) throws CredentialNotFoundException {
        if (!sessions.containsKey(token)) {
            throw new CredentialNotFoundException("User not authorized");
        }
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}