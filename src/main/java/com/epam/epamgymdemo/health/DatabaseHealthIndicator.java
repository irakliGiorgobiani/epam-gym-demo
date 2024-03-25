package com.epam.epamgymdemo.health;

import com.epam.epamgymdemo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DatabaseHealthIndicator implements HealthIndicator {

    private UserRepository userRepository;
    @Override
    public Health health() {
        try {
            userRepository.count();
            return Health.up().build();
        } catch (Exception e) {
            return Health.down().withException(e).build();
        }
    }
}
