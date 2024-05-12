package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.exception.BruteForceException;
import com.epam.epamgymdemo.exception.EntityNotFoundException;
import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialNotFoundException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${MAX_ATTEMPT}")
    private int MAX_ATTEMPT;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final HttpServletRequest httpRequest;

    public void checkCredentials(String username, String password) throws CredentialNotFoundException {
        HttpSession session = httpRequest.getSession(true);
        Map<String, Integer> attempts = (Map<String, Integer>) session.getAttribute("attempts");

        if (attempts == null) {
            attempts = new HashMap<>();
            session.setAttribute("attempts", attempts);
        }

        Integer attemptCount = attempts.getOrDefault(session.getId(), 0);

        if (session.getAttribute("blockedUntil") != null &&
                LocalDateTime.now().isBefore((LocalDateTime) session.getAttribute("blockedUntil"))) {
            throw new BruteForceException("You are blocked for another " +
                    Duration.between(LocalDateTime.now(), (LocalDateTime) session.getAttribute("blockedUntil"))
                            .toMinutes() + " minutes");
        } else if (session.getAttribute("blockedUntil") != null &&
                LocalDateTime.now().isAfter((LocalDateTime) session.getAttribute("blockedUntil"))) {
            attempts.put(session.getId(), 0);
            session.removeAttribute("blockedUntil");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException
                        (String.format("User not found with the username: %s", username)));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            attemptCount++;
            attempts.put(session.getId(), attemptCount);

            if (attemptCount == MAX_ATTEMPT) {
                session.setAttribute("blockedUntil", LocalDateTime.now().plusMinutes(5));
                throw new BruteForceException("You are blocked for 5 minutes.");
            }

            throw new CredentialNotFoundException("Incorrect username or password, try again");
        }
    }
}
