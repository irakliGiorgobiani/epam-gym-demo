package com.epam.epamgymdemo;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "com.epam.epamgymdemo.model")
@ComponentScan(basePackages = {"com.epam.epamgymdemo.aspect", "com.epam.epamgymdemo.config",
"com.epam.epamgymdemo.controller", "com.epam.epamgymdemo.converter", "com.epam.epamgymdemo.epamgymreporter",
"com.epam.epamgymdemo.error", "com.epam.epamgymdemo.exception", "com.epam.epamgymdemo.filter",
"com.epam.epamgymdemo.generator", "com.epam.epamgymdemo.health", "com.epam.epamgymdemo.metrics",
"com.epam.epamgymdemo.repository", "com.epam.epamgymdemo.service"})
@Generated
public class EpamGymDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EpamGymDemoApplication.class, args);
    }

}