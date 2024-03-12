package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Getter
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final Map<String, String> sessions = new HashMap<>();

    public String authenticateUser(String username, String password) {
        if (isValidUser(username, password)) {
            String token = generateToken();
            sessions.put(token, username);
            return token;
        }
        return null;
    }

    public boolean isValidUser(String username, String password) {
        var user = userRepository.findByUsername(username);

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

