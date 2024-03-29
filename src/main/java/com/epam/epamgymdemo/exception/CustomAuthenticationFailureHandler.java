package com.epam.epamgymdemo.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final Map<String, Integer> loginAttemptsCache = new ConcurrentHashMap<>();
    private static final int MAX_ATTEMPTS = 5;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");
        loginAttemptsCache.putIfAbsent(username, 0);
        int attempts = loginAttemptsCache.get(username) + 1;
        loginAttemptsCache.put(username, attempts);

        if (attempts >= MAX_ATTEMPTS) {
            try {
                throw new org.apache.tomcat.websocket.AuthenticationException("Too many attempts try again later");
            } catch (org.apache.tomcat.websocket.AuthenticationException e) {
                throw new RuntimeException(e);
            }
        }

        super.onAuthenticationFailure(request, response, exception);
    }
}


