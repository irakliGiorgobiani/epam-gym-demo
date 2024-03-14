package com.epam.epamgymdemo.controller;


import com.epam.epamgymdemo.model.bo.*;
import com.epam.epamgymdemo.model.dto.TraineeDto;
import com.epam.epamgymdemo.model.dto.TraineeTrainingListDto;
import com.epam.epamgymdemo.service.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;
import javax.naming.NamingException;
import javax.security.auth.login.CredentialNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/trainee/v1")
public class TraineeController {

    private final AuthenticationService authenticationService;

    private final UserService userService;

    private final TraineeService traineeService;

    private final TrainerService trainerService;

    private final TrainingTypeService trainingTypeService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody TraineeDto requestBody)
            throws NamingException, InstanceNotFoundException {
        User user = userService.create(requestBody);
        traineeService.create(user.getId(), requestBody);
        return ResponseEntity.ok(userService.usernameAndPassword(user));
    }

    @GetMapping("/{username}")
    public Map<String, Object> get(@PathVariable String username,
                                   @RequestHeader(name = "username") String usernameAuth,
                                   @RequestHeader(name = "password") String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.authenticateUser(usernameAuth, password);
        return traineeService.get(username);
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable String username,
                                      @RequestHeader(name = "username") String usernameAuth,
                                      @RequestHeader(name = "password") String password,
                                      @RequestBody TraineeDto requestBody)
            throws InstanceNotFoundException, CredentialNotFoundException, NamingException {
        authenticationService.authenticateUser(usernameAuth, password);
        userService.update(username, requestBody);
        return ResponseEntity.ok(traineeService.update(username, requestBody));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<TraineeDto> delete(@PathVariable String username,
                                         @RequestHeader(name = "username") String usernameAuth,
                                         @RequestHeader(name = "password") String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.authenticateUser(usernameAuth, password);
        TraineeDto traineeDto = traineeService.deleteByUsername(username);
        return ResponseEntity.ok(traineeDto);
    }

    @GetMapping("/{username}/notAssignedTrainers")
    public Map<Long, Map<String, Object>> getNotAssignedTrainers (@PathVariable String username,
                                                 @RequestHeader(name = "username") String usernameAuth,
                                                 @RequestHeader(name = "password") String password) throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.authenticateUser(usernameAuth, password);
        return traineeService.getUnassignedActiveTrainers(username);
    }

    @PutMapping("/update/{username}/trainersList")
    public Map<Long, Map<String, Object>> updateTrainersList(@PathVariable String username,
                                           @RequestBody List<String> usernames,
                                           @RequestHeader(name = "username") String usernameAuth,
                                           @RequestHeader(name = "password") String password)
            throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.authenticateUser(usernameAuth, password);
        return traineeService.addTrainerToTrainersList(username, trainerService.getByUsernames(usernames));
    }

    @GetMapping("/{username}/trainingsList")
    public List<TraineeTrainingListDto> getTrainingsList(@PathVariable String username,
                                                         @RequestParam(required = false) LocalDate fromDate,
                                                         @RequestParam(required = false) LocalDate toDate,
                                                         @RequestParam(required = false) String trainerName,
                                                         @RequestParam(required = false, name = "trainingType") String typeName,
                                                         @RequestHeader(name = "username") String usernameAuth,
                                                         @RequestHeader(name = "password") String password)
            throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.authenticateUser(usernameAuth, password);
        return traineeService.getTrainingsByUsernameAndCriteria(username, fromDate, toDate, trainerName,
                trainingTypeService.getTrainingTypeByName(typeName));
    }

    @PatchMapping("/{username}/changeActive/{isActive}")
    public ResponseEntity<Map<String, Object>> changeActive(@PathVariable String username, @PathVariable Boolean isActive,
                                                 @RequestHeader(name = "username") String usernameAuth,
                                               @RequestHeader(name = "password") String password)
            throws InstanceNotFoundException, CredentialNotFoundException {
        authenticationService.authenticateUser(usernameAuth, password);
        return ResponseEntity.ok(userService.changeActive(username, isActive));
    }
}
