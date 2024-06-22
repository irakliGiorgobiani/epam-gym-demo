package com.epam.epamgymdemo.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/resources/features", glue = "com.epam.epamgymdemo.cucumber.steps")
public class CucumberIntegrationTest {
}
