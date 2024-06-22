package com.epam.epamgymdemo.cucumber.steps;

import com.epam.epamgymdemo.epamgymreporter.messaging.ReporterTrainingProducer;
import com.epam.epamgymdemo.epamgymreporter.messaging.TrainingSummaryProducer;
import com.epam.epamgymdemo.model.dto.ReporterTrainingDto;
import com.epam.epamgymdemo.model.dto.TrainingSummaryDto;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;

import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@CucumberContextConfiguration
@SpringBootTest
@RequiredArgsConstructor
public class ReporterIntegrationSteps {

    private final ReporterTrainingProducer reporterTrainingProducer;

    private final TrainingSummaryProducer trainingSummaryProducer;

    private final JmsTemplate jmsTemplate;

    private ReporterTrainingDto reporterTrainingDto;

    @Given("a training data with the username {string}")
    public void givenTrainingData(String username) {
        reporterTrainingDto = ReporterTrainingDto.builder()
                .username(username)
                .firstName("john")
                .lastName("doe")
                .isActive(true)
                .trainingDate(LocalDate.now())
                .trainingDuration(2)
                .build();
    }

    @When("the Gym microservice sends the data to the Reporter microservice")
    public void whenGymSendsData() {
        reporterTrainingProducer.send(reporterTrainingDto);
    }

    @Then("the Reporter microservice should save the training summary with the username {string}")
    public void thenReporterSavesData(String username) {
        TrainingSummaryDto receivedSummary = (TrainingSummaryDto) jmsTemplate
                .receiveAndConvert("training.summary.response.queue");
        assert receivedSummary != null;
        assertEquals(username, receivedSummary.getUsername());
    }

    @Given("a training summary exists in the Reporter microservice for username {string}")
    public void givenTrainingSummaryExists(String username) {
        TrainingSummaryDto trainingSummaryDto = TrainingSummaryDto.builder()
                .username(username)
                .firstName("John")
                .lastName("Doe")
                .status(true)
                .yearlySummary(new HashMap<>())
                .build();
        jmsTemplate.convertAndSend("training.summary.queue", trainingSummaryDto);
    }

    @When("the Gym microservice requests the training summary for username {string}")
    public void whenGymRequestsData(String username) {
        trainingSummaryProducer.send(username);
    }

    @Then("the Gym microservice should receive the training summary with the username {string}")
    public void thenGymReceivesData(String username) {
        TrainingSummaryDto receivedSummary = (TrainingSummaryDto) jmsTemplate
                .receiveAndConvert("training.summary.response.queue");
        assert receivedSummary != null;
        assertEquals(username, receivedSummary.getUsername());
    }

    @Given("a malformed training data with the username {string}")
    public void givenMalformedTrainingData(String username) {
        reporterTrainingDto = ReporterTrainingDto.builder()
                .username(username)
                .build();
    }

    @Then("an error should be returned indicating the data is invalid")
    public void thenAnErrorShouldBeReturnedIndicatingTheDataIsInvalid() {
        assertThrows(ConstraintViolationException.class, () -> jmsTemplate
                .receiveAndConvert("training.summary.response.queue"));
    }
}
