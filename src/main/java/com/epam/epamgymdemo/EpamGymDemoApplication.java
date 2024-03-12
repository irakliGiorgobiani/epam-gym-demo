package com.epam.epamgymdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "com.epam.epamgymdemo.model")
@ComponentScan(basePackages = {"com.epam.epamgymdemo.controller", "com.epam.epamgymdemo.service",
        "com.epam.epamgymdemo.facade", "com.epam.epamgymdemo.repository", "com.epam.epamgymdemo.generator"})
public class EpamGymDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EpamGymDemoApplication.class, args);
    }

}
