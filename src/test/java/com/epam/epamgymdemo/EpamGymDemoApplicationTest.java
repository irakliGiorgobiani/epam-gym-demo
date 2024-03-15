package com.epam.epamgymdemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@SpringBootApplication
@EntityScan(basePackages = "com.epam.epamgymdemo.model")
@ComponentScan(basePackages = {"com.epam.epamgymdemo.controller", "com.epam.epamgymdemo.service",
        "com.epam.epamgymdemo.exception", "com.epam.epamgymdemo.repository", "com.epam.epamgymdemo.generator",
        "com.epam.epamgymdemo.config", "com.epam.epamgymdemo.aspect"})
class EpamGymDemoApplicationTest {

    @Test
    void testMain() {
        assertDoesNotThrow(() -> SpringApplication.run(EpamGymDemoApplication.class));
    }
}
