package com.epam.epamgymdemo.cucumber.steps;

import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.service.AuthenticationService;
import com.epam.epamgymdemo.service.UserService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.security.auth.login.CredentialNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
public class UserAuthenticationSteps {

    private final AuthenticationService authenticationService;

    private final UserService userService;

    private String username;

    private String password;

    @Given("username {string} and password {string}")
    public void usernameAndPassword(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @When("checking credentials")
    public void checkingCredentials() throws CredentialNotFoundException {
        authenticationService.checkCredentials(username, password);
    }

    @Then("authentication is successful")
    public void authenticationIsSuccessful() {
        User correctUser = userService.getByUsername(username);
        assertEquals(correctUser.getPassword(), password);
    }

    @Then("authentication is unsuccessful, because of the incorrect username")
    public void authenticationIsUnsuccessful() {
        assertThrows(UsernameNotFoundException.class, () -> userService.getByUsername(username));
    }

    @Then("authentication is unsuccessful, because of the incorrect password")
    public void authenticationIsUnsuccessfulBecauseOfIncorrectPassword() {
        User incorrectUser = userService.getByUsername(username);
        assertNotEquals(incorrectUser.getPassword(), password);
    }
}
