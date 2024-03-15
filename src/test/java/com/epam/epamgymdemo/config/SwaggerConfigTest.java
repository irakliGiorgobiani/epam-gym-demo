package com.epam.epamgymdemo.config;

import org.junit.jupiter.api.Test;
import org.springdoc.core.models.GroupedOpenApi;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SwaggerConfigTest {

    @Test
    void testControllerApi() {
        SwaggerConfig swaggerConfig = new SwaggerConfig();
        GroupedOpenApi groupedOpenApi = swaggerConfig.controllerApi();

        assertEquals("controller-api", groupedOpenApi.getGroup());
        assertEquals("com.epam.epamgymdemo.controller", groupedOpenApi.getPackagesToScan().get(0));
    }
}

