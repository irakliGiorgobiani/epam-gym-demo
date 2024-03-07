package com.epam.epamgymdemo.controller;

import com.epam.epamgymdemo.facade.GymFacade;
import com.epam.epamgymdemo.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/trainee")
public class TraineeController {

    private final GymFacade gymFacade;

    public TraineeController(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> requestBody) {
        String firstName = requestBody.get("firstName");
        String lastName = requestBody.get("lastName");
        LocalDate dateOfBirth = LocalDate.parse(requestBody.get("dateOfBirth"));
        String address = requestBody.get("address");

        Trainee trainee = gymFacade.createTrainee(dateOfBirth, address, firstName, lastName, true);

        return Map.of("username", trainee.getUser().getUserName(),
                "password", trainee.getUser().getPassword());
    }

    @GetMapping("/{username}")
    public Map<String, Object> get(@PathVariable String username,
                                   @RequestHeader(name = "username") String usernameAuth,
                                   @RequestHeader(name = "password") String password) throws InstanceNotFoundException {
        Trainee trainee = gymFacade.selectTraineeByUsername(username, usernameAuth, password);
        User user = trainee.getUser();

        return Map.of("firstName", user.getFirstName(), "lastName", user.getLastName(),
                "dateOfBirth", trainee.getDateOfBirth(), "address", trainee.getAddress(),
                "isActive", user.getIsActive(), "trainersList", trainee.getTrainers().stream()
                        .map(t -> Map.of("username", t.getUser().getUserName(), "firstName", t.getUser().getFirstName(),
                                "lastName", t.getUser().getLastName(), "specialization", t.getTrainingType().getId()))
                        .toList());
    }

    @PutMapping("/update")
    public Map<String, Object> update(@RequestHeader(name = "username") String oldUsername, @RequestHeader(name = "password") String password,
                                      @RequestBody Map<String, String> requestBody) throws InstanceNotFoundException {
        String newUsername = requestBody.get("username");
        String firstName = requestBody.get("firstName");
        String lastName = requestBody.get("lastName");
        LocalDate dateOfBirth = null;
        if (requestBody.containsKey("dateOfBirth")) {
            dateOfBirth = LocalDate.parse(requestBody.get("dateOfBirth"));
        }
        String address = null;
        if (requestBody.containsKey("address")) {
            address = requestBody.get("address");
        }
        Boolean isActive = Boolean.parseBoolean(requestBody.get("isActive"));

        Trainee trainee = gymFacade.selectTraineeByUsername(oldUsername, oldUsername, password);
        User user = trainee.getUser();

        gymFacade.updateTrainee(trainee.getId(), dateOfBirth, address, user.getId(), oldUsername, password);
        gymFacade.changeTraineesIsActive(trainee.getId(), isActive, oldUsername, password);
        gymFacade.changeTraineesFirstName(trainee.getId(), firstName, oldUsername, password);
        gymFacade.changeTraineesLastName(trainee.getId(), lastName, oldUsername, password);
        gymFacade.changeTraineesUsername(trainee.getId(), newUsername, oldUsername, password);

        return Map.of("username", user.getUserName(),"firstName", user.getFirstName(), "lastName", user.getLastName(),
                "dateOfBirth", trainee.getDateOfBirth(), "address", trainee.getAddress(),
                "isActive", user.getIsActive(), "trainersList", trainee.getTrainers().stream()
                        .map(t -> Map.of("username", t.getUser().getUserName(), "firstName", t.getUser().getFirstName(),
                                "lastName", t.getUser().getLastName(), "specialization", t.getTrainingType().getId()))
                        .toList());
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> delete(@PathVariable String username,
                                         @RequestHeader(name = "username") String usernameAuth,
                                         @RequestHeader(name = "password") String password) throws InstanceNotFoundException {
        gymFacade.deleteTraineeByUsername(username, usernameAuth, password);

        return ResponseEntity.ok(String.format("Trainee with the username: %s has been successfully deleted", username));
    }

    @GetMapping("/{username}/notAssignedTrainers")
    public List<Trainer> getNotAssignedTrainers (@PathVariable String username,
                                                 @RequestHeader(name = "username") String usernameAuth,
                                                 @RequestHeader(name = "password") String password) throws InstanceNotFoundException {
        return gymFacade.selectTrainersUnassignedToTrainee(username, usernameAuth, password);
    }

    @PutMapping("/update/trainersList")
    public Set<Trainer> updateTrainersList(@RequestBody Map<String, Object> requestBody,
                                           @RequestHeader(name = "username") String usernameAuth,
                                           @RequestHeader(name = "password") String password) throws InstanceNotFoundException {
        String username = (String) requestBody.get("username");
        List<Trainer> trainers = ((List<String>) requestBody.get("trainersList")).stream()
                    .map(t -> {
                        try {
                            return gymFacade.selectTrainerByUsername(t, usernameAuth, password);
                        } catch (InstanceNotFoundException e) {
                            throw new RuntimeException("One or more usernames are incorrect", e);
                        }
                    })
                    .toList();

        Long id = gymFacade.selectTraineeByUsername(username, usernameAuth, password).getId();

        for (Trainer trainer : trainers) {
            gymFacade.addTrainerToTrainersList(id, trainer, usernameAuth, password);
        }

        return gymFacade.selectTraineesTrainerList(id, usernameAuth, password);
    }

    @GetMapping("/{username}/trainingsList")
    public List<Training> getTrainingsList(@PathVariable String username,
                                           @RequestParam(required = false) LocalDate fromDate,
                                           @RequestParam(required = false) LocalDate toDate,
                                           @RequestParam(required = false) String trainerName,
                                           @RequestParam(required = false, name = "trainingType") String typeName,
                                           @RequestHeader(name = "username") String usernameAuth,
                                           @RequestHeader(name = "password") String password) throws InstanceNotFoundException {
        TrainingType trainingType = null;
        if (typeName != null) {
            trainingType = gymFacade.getTrainingTypeByName(typeName, usernameAuth, password);
        }

        return gymFacade.selectTraineeTrainingsByUsernameAndCriteria(username, fromDate, toDate, trainerName, trainingType, usernameAuth, password);
    }
}
