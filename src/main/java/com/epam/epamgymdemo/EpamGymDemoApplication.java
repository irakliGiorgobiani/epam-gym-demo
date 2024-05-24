package com.epam.epamgymdemo;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "com.epam.epamgymdemo.model")
@ComponentScan(basePackages = {"com.epam.epamgymdemo.controller", "com.epam.epamgymdemo.service",
        "com.epam.epamgymdemo.exception", "com.epam.epamgymdemo.repository", "com.epam.epamgymdemo.generator",
        "com.epam.epamgymdemo.config", "com.epam.epamgymdemo.aspect",
        "com.epam.epamgymdemo.health", "com.epam.epamgymdemo.metrics",
        "com.epam.epamgymdemo.filter"})
@Generated
@EnableFeignClients
public class EpamGymDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EpamGymDemoApplication.class, args);
    }

}