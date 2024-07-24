package com.epam.epamgymdemo.cucumber.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
public class UserPermissionSteps {

    @LocalServerPort
    private int port;

    private final String url = "http://localhost:" + port + "/trainee/v1/Irakli.Giorgobiani";

    private final RestTemplate restTemplate;

    private ResponseEntity<String> response;

    @Given("a request with a valid JWT token")
    public void aRequestWithValidToken() {
        String validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG" +
                "4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c\n";
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().set("Authorization", "Bearer " + validToken);
            return execution.execute(request, body);
        });
    }

    @Given("a request with an invalid JWT token")
    public void aRequestWithInvalidToken() {
        String invalidToken = "invalidToken";
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().set("Authorization", "Bearer " + invalidToken);
            return execution.execute(request, body);
        });
    }

    @When("making a request to the secured endpoint")
    public void makingRequestToSecuredEndpoint() {
        response = restTemplate.getForEntity(url, String.class);
    }

    @Then("the request should be successful")
    public void requestShouldBeSuccessful() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Then("the request should be unauthorized")
    public void requestShouldBeUnauthorized() {
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Then("an unauthorized exception should be thrown")
    public void unauthorizedExceptionShouldBeThrown() {
        assertThrows(HttpClientErrorException.Unauthorized.class, () -> restTemplate.getForEntity(url, String.class));
    }
}
