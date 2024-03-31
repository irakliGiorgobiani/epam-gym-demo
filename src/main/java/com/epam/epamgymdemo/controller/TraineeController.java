package com.epam.epamgymdemo.controller;


import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.model.dto.ActiveDto;
import com.epam.epamgymdemo.model.dto.TraineeDto;
import com.epam.epamgymdemo.model.dto.TrainerDto;
import com.epam.epamgymdemo.model.dto.TraineeTrainingDto;
import com.epam.epamgymdemo.model.dto.UsernamePasswordTokenDto;
import com.epam.epamgymdemo.service.JwtService;
import com.epam.epamgymdemo.service.TraineeService;
import com.epam.epamgymdemo.service.TrainerService;
import com.epam.epamgymdemo.service.TrainingTypeService;
import com.epam.epamgymdemo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/trainee/v1")
@Tag(name = "Trainee",
        description = "Operations for creating, updating, retrieving and deleting trainees in the application")
public class TraineeController {

    private final UserService userService;

    private final TraineeService traineeService;

    private final TrainerService trainerService;

    private final TrainingTypeService trainingTypeService;

    private final JwtService jwtService;

    @PostMapping("/register")
    @Transactional
    @Operation(summary = "Creating a new trainee",
            description = "Create a new trainee and a user associated to that trainee " +
                    "with given fields from the request body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered a new trainee"),
            @ApiResponse(responseCode = "400", description = "Either naming is not valid or " +
                    "at least one field was not given")
    })
    public ResponseEntity<UsernamePasswordTokenDto> register(@RequestBody TraineeDto requestBody) {
        User user = userService.create(requestBody);
        traineeService.create(user.getId(), requestBody);
        return ResponseEntity.ok(userService.usernameAndPassword(user, jwtService));
    }

    @GetMapping("/{username}")
    @Operation(summary = "Retrieving a trainee",
            description = "Retrieve the trainee by username with its fields and " +
                    "also the user fields associated with that trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved the trainee with the given username"),
            @ApiResponse(responseCode = "401", description = "Invalid Username or password"),
            @ApiResponse(responseCode = "404",
                    description = "The trainee instance with the given username does not exist")
    })
    public ResponseEntity<TraineeDto> get(@PathVariable String username) {
        return ResponseEntity.ok(traineeService.get(username));
    }

    @PutMapping("/{username}")
    @Operation(summary = "Updating a trainee",
            description = "Update the trainee and the user associated with that trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully updated the trainee with the given username"),
            @ApiResponse(responseCode = "401", description = "Invalid Username or password"),
            @ApiResponse(responseCode = "404",
                    description = "The trainee instance with the given username does not exist")
    })
    public ResponseEntity<TraineeDto> update(@PathVariable String username,
                                      @RequestBody TraineeDto requestBody) {
        userService.update(username, requestBody);
        return ResponseEntity.ok(traineeService.update(username, requestBody));
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Deleting a trainee",
            description = "Delete the trainee, the trainings which the trainee is involved in and " +
                    "the user associated with the trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully deleted the trainee with the given username"),
            @ApiResponse(responseCode = "401", description = "Invalid Username or password"),
            @ApiResponse(responseCode = "404",
                    description = "The trainee instance with the given username does not exist")
    })
    public ResponseEntity<TraineeDto> delete(@PathVariable String username) {
        return ResponseEntity.ok(traineeService.delete(username));
    }

    @GetMapping("/{username}/not-assigned-trainers")
    @Operation(summary = "Retrieving active trainers which are not assigned to a trainee",
            description = "get all active trainers which are not assigned to the trainee, " +
                    "which has the username given in the path variable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the trainers which are not " +
                    "assigned to the trainee with the given username"),
            @ApiResponse(responseCode = "401", description = "Invalid Username or password"),
            @ApiResponse(responseCode = "404",
                    description = "The trainee instance with the given username does not exist")
    })
    public ResponseEntity<Set<TrainerDto>> getNotAssignedTrainers (@PathVariable String username) {
        return ResponseEntity.ok(traineeService.getUnassignedActiveTrainers(username));
    }

    @PutMapping("/{username}/trainer-list")
    @Operation(summary = "Retrieving trainers that are assigned to a trainee",
            description = "Update the trainer list of the trainee, by giving the usernames of the trainers, " +
                    "which are not assigned to the trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved trainers which are " +
                    "assigned to the trainee with the given username"),
            @ApiResponse(responseCode = "401", description = "Invalid Username or password"),
            @ApiResponse(responseCode = "404",
                    description = "The trainee instance with the given username does not exist")
    })
    public ResponseEntity<Set<TrainerDto>> updateTrainerList(@PathVariable String username,
                                             @RequestBody List<String> trainerUsernames) {
        return ResponseEntity.ok(traineeService
                .addTrainerToTrainersList(username, trainerService.getByUsernames(trainerUsernames)));
    }

    @GetMapping("/{username}/trainings-list")
    @Operation(summary = "Retrieving trainings that a trainee is involved in",
            description = "get all trainings that the trainee is involved in, also providing optional parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved trainings which are " +
                    "scheduled for the trainee with the given username"),
            @ApiResponse(responseCode = "401", description = "Invalid Username or password"),
            @ApiResponse(responseCode = "404",
                    description = "The trainee instance with the given username does not exist")
    })
    public ResponseEntity<List<TraineeTrainingDto>> getTrainingList(@PathVariable String username,
                                                                    @RequestParam(required = false) LocalDate fromDate,
                                                                    @RequestParam(required = false) LocalDate toDate,
                                                                    @RequestParam(required = false) String trainerName,
                                                                    @RequestParam(required = false,
                                                                 name = "trainingType") String typeName) {
        return ResponseEntity.ok(traineeService.getTrainingsByUsernameAndCriteria(username, fromDate,
                toDate, trainerName, trainingTypeService.getTrainingTypeByName(typeName)));
    }

    @PatchMapping("/{username}/change-activity/{isActive}")
    @Operation(summary = "Changing the isActive field for a trainee",
            description = "change the isActive field of the trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully changed the isActive field " +
                    "for the trainee with the given username"),
            @ApiResponse(responseCode = "401", description = "Invalid Username or password"),
            @ApiResponse(responseCode = "404", description = "The trainee instance with the given username does not exist")
    })
    public ResponseEntity<ActiveDto> changeActive(@PathVariable String username,
                                                  @PathVariable Boolean isActive) {
        return ResponseEntity.ok(userService.changeActive(username, isActive));
    }
}