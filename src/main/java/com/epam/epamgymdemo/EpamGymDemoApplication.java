package com.epam.epamgymdemo;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EntityScan(basePackages = "com.epam.epamgymdemo.model")
@Generated
@EnableFeignClients
public class EpamGymDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EpamGymDemoApplication.class, args);
    }

}