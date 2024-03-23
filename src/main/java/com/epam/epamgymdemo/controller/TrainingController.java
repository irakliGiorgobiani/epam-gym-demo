package com.epam.epamgymdemo.controller;

import com.epam.epamgymdemo.model.dto.TrainingDto;
import com.epam.epamgymdemo.service.AuthenticationService;
import com.epam.epamgymdemo.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.CredentialNotFoundException;

@RestController
@AllArgsConstructor
@RequestMapping("/training/v1")
public class TrainingController {

    private final AuthenticationService authenticationService;

    private final TrainingService trainingService;

    @PostMapping("/add")
    @Operation(summary = "Creating a training",
            description = "Create a new training with the given fields from the request body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully created the training with the given fields: " +
                    "traineeUsername, trainerUsername, trainingDuration, trainingDate, trainingName"),
            @ApiResponse(responseCode = "401", description = "Invalid Username or password"),
            @ApiResponse(responseCode = "404",
                    description = "The trainer instance with the given username does not exist or " +
                    "the trainee with the given username does not exist")
    })
    public ResponseEntity<String> addTraining(@RequestBody TrainingDto requestBody,
                                                          @RequestHeader(name = "username") String username,
                                                          @RequestHeader(name = "password") String password)
            throws CredentialNotFoundException {
        authenticationService.authenticateUser(username, password);
        trainingService.create(requestBody);
        return ResponseEntity.ok("Training has been added");
    }
}