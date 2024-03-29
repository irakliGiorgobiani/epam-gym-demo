package com.epam.epamgymdemo.controller;

import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.model.dto.ActiveDto;
import com.epam.epamgymdemo.model.dto.TrainerDto;
import com.epam.epamgymdemo.model.dto.TrainerTrainingDto;
import com.epam.epamgymdemo.model.dto.TrainerWithListDto;
import com.epam.epamgymdemo.model.dto.UsernamePasswordTokenDto;
import com.epam.epamgymdemo.service.AuthenticationService;
import com.epam.epamgymdemo.service.JwtService;
import com.epam.epamgymdemo.service.TrainerService;
import com.epam.epamgymdemo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/trainer/v1")
public class TrainerController {

    private final AuthenticationService authenticationService;

    private final UserService userService;

    private final TrainerService trainerService;

    private final JwtService jwtService;

    @PostMapping("/register")
    @Transactional
    @Operation(summary = "Creating a new trainer",
            description = "Register a new trainer and also a new user associated to the trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered a new trainer"),
            @ApiResponse(responseCode = "400",
                    description = "Either naming is not valid or at least one field was not given")
    })
    public ResponseEntity<UsernamePasswordTokenDto> register(@RequestBody TrainerDto requestBody) {
        User user = userService.create(requestBody);
        trainerService.create(user.getId(), requestBody.getSpecializationId());
        return ResponseEntity.ok(userService.usernameAndPassword(user, jwtService));
    }

    @GetMapping("/{username}")
    @Operation(summary = "Retrieving a trainer",
            description = "Get the trainer and its fields and also the users fields associated to the trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved the trainer with the given username"),
            @ApiResponse(responseCode = "401", description = "Invalid Username or password"),
            @ApiResponse(responseCode = "404",
                    description = "The trainer instance with the given username does not exist")
    })
    public ResponseEntity<TrainerWithListDto> get(@PathVariable String username,
                                   @RequestHeader(name = "username") String usernameAuth,
                                   @RequestHeader(name = "password") String password) {
        authenticationService.authenticateUser(usernameAuth, password);
        return ResponseEntity.ok(trainerService.get(username));
    }

    @PutMapping("/{username}")
    @Operation(summary = "Updating a trainer",
            description = "Update the trainer and also the user associated to the trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully updated the trainer with the given username"),
            @ApiResponse(responseCode = "401", description = "Invalid Username or password"),
            @ApiResponse(responseCode = "404",
                    description = "The trainer instance with the given username does not exist")
    })
    public ResponseEntity<TrainerWithListDto> update(@PathVariable String username,
                                                     @RequestHeader(name = "username") String usernameAuth,
                                                     @RequestHeader(name = "password") String password,
                                                     @RequestBody TrainerDto requestBody) {
        authenticationService.authenticateUser(usernameAuth, password);
        userService.update(username, requestBody);
        return ResponseEntity.ok(trainerService.update(username));
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "Retrieving trainings related to a trainer",
            description = "get all trainings that the trainer is involved in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the trainings " +
                    "related to the trainer with the given username"),
            @ApiResponse(responseCode = "401", description = "Invalid Username or password"),
            @ApiResponse(responseCode = "404",
                    description = "The trainer instance with the given username does not exist")
    })
    public ResponseEntity<List<TrainerTrainingDto>> getTrainingList(@PathVariable String username,
                                                                    @RequestParam(required = false) LocalDate fromDate,
                                                                    @RequestParam(required = false) LocalDate toDate,
                                                                    @RequestParam(required = false) String traineeName,
                                                                    @RequestHeader(name = "username")
                                                                        String usernameAuth,
                                                                    @RequestHeader(name = "password") String password) {
        authenticationService.authenticateUser(usernameAuth, password);
        return ResponseEntity.ok(trainerService.getTrainingsByUsernameAndCriteria(username, fromDate,
                toDate, traineeName));
    }

    @PatchMapping("/{username}/change-active/{isActive}")
    @Operation(summary = "Updating the isActive field for a trainer",
            description = "Change the isActive field of the trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the isActive field " +
                    "for the trainer with the given username"),
            @ApiResponse(responseCode = "401", description = "Invalid Username or password"),
            @ApiResponse(responseCode = "404",
                    description = "The trainer instance with the given username does not exist")
    })
    public ResponseEntity<ActiveDto> changeActive(@PathVariable String username,
                                                    @PathVariable Boolean isActive,
                                                    @RequestHeader(name = "username") String usernameAuth,
                                                    @RequestHeader(name = "password") String password) {
        authenticationService.authenticateUser(usernameAuth, password);
        return ResponseEntity.ok(userService.changeActive(username, isActive));
    }
}