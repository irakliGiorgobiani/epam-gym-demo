package com.epam.epamgymdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.epam.epamgymdemo.model")
public class EpamGymDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EpamGymDemoApplication.class, args);
    }

}
