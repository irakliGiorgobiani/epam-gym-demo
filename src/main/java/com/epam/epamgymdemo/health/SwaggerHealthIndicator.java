package com.epam.epamgymdemo.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SwaggerHealthIndicator implements HealthIndicator {

    private static final String SWAGGER_URL = "http://localhost:8080/swagger-ui.html";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Health health() {
        try {
            restTemplate.getForObject(SWAGGER_URL, String.class);
            return Health.up().build();
        } catch (Exception ex) {
            return Health.down().withException(ex).build();
        }
    }
}
