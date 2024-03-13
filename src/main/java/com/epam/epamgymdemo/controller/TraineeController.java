package com.epam.epamgymdemo.controller;


import com.epam.epamgymdemo.model.bo.*;
import com.epam.epamgymdemo.service.TraineeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;
import javax.naming.NamingException;
import javax.security.auth.login.CredentialNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/trainee")
public class TraineeController {

    private final TraineeService traineeService;

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> requestBody) throws InstanceNotFoundException, NamingException {
        String firstName = requestBody.get("firstName");
        String lastName = requestBody.get("lastName");
        LocalDate birthday = LocalDate.parse(requestBody.get("birthday"));
        String address = requestBody.get("address");

        Trainee trainee = gymFacade.createTrainee(birthday, address, firstName, lastName, true);

        return Map.of("username", trainee.getUser().getUsername(),
                "password", trainee.getUser().getPassword());
    }

    @GetMapping("/{username}")
    public Map<String, Object> get(@PathVariable String username,
                                   @RequestHeader(name = "username") String usernameAuth,
                                   @RequestHeader(name = "password") String password) throws InstanceNotFoundException, CredentialNotFoundException {
        String token = gymFacade.authenticate(usernameAuth, password);

        Trainee trainee = gymFacade.getTraineeByUsername(username, token);
        User user = trainee.getUser();

        return Map.of("firstName", user.getFirstName(), "lastName", user.getLastName(),
                "dateOfBirth", trainee.getBirthday(), "address", trainee.getAddress(),
                "isActive", user.getIsActive(), "trainersList", trainee.getTrainers().stream()
                        .map(t -> Map.of("username", t.getUser().getUsername(), "firstName", t.getUser().getFirstName(),
                                "lastName", t.getUser().getLastName(), "specialization", t.getTrainingType().getId()))
                        .toList());
    }

    @PutMapping("/update/{id}")
    public Map<String, Object> update(@RequestHeader(name = "username") String oldUsername, @RequestHeader(name = "password") String password,
                                      @RequestBody Map<String, String> requestBody, @PathVariable Long id) throws InstanceNotFoundException, CredentialNotFoundException {
        String token = gymFacade.authenticate(oldUsername, password);

        String newUsername = requestBody.get("username");
        String firstName = requestBody.get("firstName");
        String lastName = requestBody.get("lastName");
        LocalDate birthday = null;
        if (requestBody.containsKey("birthday")) {
            birthday = LocalDate.parse(requestBody.get("birthday"));
        }
        String address = null;
        if (requestBody.containsKey("address")) {
            address = requestBody.get("address");
        }
        Boolean isActive = Boolean.parseBoolean(requestBody.get("isActive"));

        Trainee trainee = gymFacade.getTraineeById(id, token);
        Long id = trainee.getId();
        User user = trainee.getUser();

        gymFacade.updateTrainee(id, birthday, address, user.getId(), token);
        gymFacade.changeTraineesIsActive(id, token);
        gymFacade.changeTraineesFirstName(id, firstName, token);
        gymFacade.changeTraineesLastName(id, lastName, token);
        gymFacade.changeTraineesUsername(id, newUsername, token);

        return Map.of("username", user.getUsername(),"firstName", user.getFirstName(), "lastName", user.getLastName(),
                "birthday", trainee.getBirthday(), "address", trainee.getAddress(),
                "isActive", user.getIsActive(), "trainersList", trainee.getTrainers().stream()
                        .map(t -> Map.of("username", t.getUser().getUsername(), "firstName", t.getUser().getFirstName(),
                                "lastName", t.getUser().getLastName(), "specialization", t.getTrainingType().getId()))
                        .toList());
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> delete(@PathVariable String username,
                                         @RequestHeader(name = "username") String usernameAuth,
                                         @RequestHeader(name = "password") String password) throws InstanceNotFoundException, CredentialNotFoundException {
        String token = gymFacade.authenticate(usernameAuth, password);

        gymFacade.deleteTraineeByUsername(username, token);

        return ResponseEntity.ok(String.format("Trainee with the username: %s has been successfully deleted", username));
    }

    @GetMapping("/{username}/notAssignedTrainers")
    public List<Trainer> getNotAssignedTrainers (@PathVariable String username,
                                                 @RequestHeader(name = "username") String usernameAuth,
                                                 @RequestHeader(name = "password") String password) throws InstanceNotFoundException, CredentialNotFoundException {
        String token = gymFacade.authenticate(usernameAuth, password);

        return gymFacade.getTrainersUnassignedToTrainee(username, token);
    }

    @PutMapping("/update/trainersList")
    public Set<Trainer> updateTrainersList(@RequestBody Map<String, Object> requestBody,
                                           @RequestHeader(name = "username") String usernameAuth,
                                           @RequestHeader(name = "password") String password) throws InstanceNotFoundException, CredentialNotFoundException {
        String token = gymFacade.authenticate(usernameAuth, password);

        String username = (String) requestBody.get("username");
        List<Trainer> trainers = ((List<String>) requestBody.get("trainersList")).stream()
                    .map(t -> {
                        try {
                            return gymFacade.getTrainerByUsername(t, token);
                        } catch (InstanceNotFoundException e) {
                            throw new RuntimeException("One or more usernames are incorrect", e);
                        } catch (CredentialNotFoundException e) {
                            throw new RuntimeException("User is not authenticated", e);
                        }
                    })
                    .toList();

        Long id = gymFacade.getTraineeByUsername(username, token).getId();

        for (Trainer trainer : trainers) {
            gymFacade.addTrainerToTrainersList(id, trainer, token);
        }

        return gymFacade.getTraineesTrainerList(id, token);
    }

    @GetMapping("/{username}/trainingsList")
    public List<Training> getTrainingsList(@PathVariable String username,
                                           @RequestParam(required = false) LocalDate fromDate,
                                           @RequestParam(required = false) LocalDate toDate,
                                           @RequestParam(required = false) String trainerName,
                                           @RequestParam(required = false, name = "trainingType") String typeName,
                                           @RequestHeader(name = "username") String usernameAuth,
                                           @RequestHeader(name = "password") String password) throws InstanceNotFoundException, CredentialNotFoundException {
        String token = gymFacade.authenticate(usernameAuth, password);

        TrainingType trainingType = null;
        if (typeName != null) {
            trainingType = gymFacade.getTrainingTypeByName(typeName, token);
        }

        return gymFacade.getTraineeTrainingsByUsernameAndCriteria(username, fromDate, toDate, trainerName, trainingType, token);
    }

    @PatchMapping("/{username}/change-isActive/{isActive}")
    public ResponseEntity<String> changeIsActive(@PathVariable String username, @PathVariable Boolean isActive,
                                                 @RequestHeader(name = "username") String usernameAuth, @RequestHeader(name = "password") String password) throws InstanceNotFoundException, CredentialNotFoundException {
        String token = gymFacade.authenticate(usernameAuth, password);

        Long id = gymFacade.getTraineeByUsername(username, token).getId();

        gymFacade.changeTraineesIsActive(id, token);

        return ResponseEntity.ok(String.format("isActive changed successfully for the trainee with the username: %s", username));
    }
}
