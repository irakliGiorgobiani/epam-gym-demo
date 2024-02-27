package com.epam.epamgymdemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class EpamGymDemoApplicationTests {

    @Test
    void contextLoads() {
        ApplicationContext context = new SpringApplicationBuilder(EpamGymDemoApplication.class).run();
        assertNotEquals(context, null);
    }
}
