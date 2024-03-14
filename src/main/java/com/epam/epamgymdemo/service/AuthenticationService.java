package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialNotFoundException;

@Service
@Getter
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    public void authenticateUser(String username, String password) throws CredentialNotFoundException {
        if (!isValidUser(username, password)) {
            throw new CredentialNotFoundException("Invalid username or password");
        }
    }

    public boolean isValidUser(String username, String password) {
        var user = userRepository.findByUsername(username);

        return user != null && user.getPassword().equals(password);
    }
}

