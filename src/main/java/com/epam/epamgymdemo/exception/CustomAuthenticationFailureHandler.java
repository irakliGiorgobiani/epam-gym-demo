package com.epam.epamgymdemo.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final Map<String, Integer> loginAttemptsCache = new ConcurrentHashMap<>();
    private static final int MAX_ATTEMPTS = 3;
    private static final int BLOCK_DURATION_MINUTES = 5;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");
        int attempts = loginAttemptsCache.getOrDefault(username, 0) + 1;
        loginAttemptsCache.put(username, attempts);

        if (attempts >= MAX_ATTEMPTS) {
            long blockEndTime = System.currentTimeMillis() + BLOCK_DURATION_MINUTES * 60 * 1000;
            loginAttemptsCache.put(username, -1);
            request.setAttribute("blockEndTime", blockEndTime);
            response.sendRedirect("auth/v1/login-error");
            return;
        }
        this.setDefaultFailureUrl("/auth/v1/login-error");
        super.onAuthenticationFailure(request, response, exception);
    }
}
