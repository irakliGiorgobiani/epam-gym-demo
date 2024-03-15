package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.exception.MissingInstanceException;
import com.epam.epamgymdemo.model.dto.UsernamePasswordDto;
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
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new MissingInstanceException
                        (String.format("User not found with the username: %s", username)));

        return user != null && user.getPassword().equals(password);
    }

    public UsernamePasswordDto usernamePassword(String username, String password) {
        return UsernamePasswordDto.builder().username(username).password(password).build();
    }
}