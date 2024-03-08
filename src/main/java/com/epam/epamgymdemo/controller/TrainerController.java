package com.epam.epamgymdemo.controller;

import com.epam.epamgymdemo.facade.GymFacade;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;
import javax.security.auth.login.CredentialNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/trainers")
public class TrainerController {
    private final GymFacade gymFacade;

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> requestBody) throws InstanceNotFoundException {
        String firstName = requestBody.get("firstName");
        String lastName = requestBody.get("lastName");
        Long trainingType = Long.parseLong(requestBody.get("specialization"));

        Trainer trainer = gymFacade.createTrainer(trainingType, firstName, lastName, true);

        return Map.of("username", trainer.getUser().getUsername(),
                "password", trainer.getUser().getPassword());
    }

    @GetMapping("/{username}")
    public Map<String, Object> get(@PathVariable String username,
                                   @RequestHeader(name = "username") String usernameAuth, @RequestHeader(name = "password") String password) throws InstanceNotFoundException, CredentialNotFoundException {
        String token = gymFacade.authenticate(usernameAuth, password);

        Trainer trainer = gymFacade.getTrainerByUsername(username, token);
        User user = trainer.getUser();

        return Map.of("firstName", user.getFirstName(), "lastName", user.getLastName(),
                "specialization", trainer.getTrainingType().getId(), "isActive", user.getIsActive(),
                "traineesList", trainer.getTrainees().stream()
                        .map(t -> Map.of("username", t.getUser().getUsername(), "firstName", t.getUser().getFirstName(),
                                "lastName", t.getUser().getLastName()))
                        .toList());
    }

    @PutMapping("/update")
    public Map<String, Object> update(@RequestHeader(name = "username") String oldUsername, @RequestHeader(name = "password") String password,
                                      @RequestBody Map<String, String> requestBody) throws InstanceNotFoundException, CredentialNotFoundException {
        String token = gymFacade.authenticate(oldUsername, password);

        String newUsername = requestBody.get("username");
        String firstName = requestBody.get("firstName");
        String lastName = requestBody.get("lastName");
        Long trainingType = Long.parseLong(requestBody.get("specialization"));
        Boolean isActive = Boolean.parseBoolean(requestBody.get("isActive"));

        Trainer trainer = gymFacade.getTrainerByUsername(oldUsername, token);
        User user = trainer.getUser();

        gymFacade.updateTrainer(trainer.getId(), trainer.getTrainingType().getId(), user.getId(), token);
        gymFacade.changeTrainersIsActive(trainer.getId(), isActive, token);
        gymFacade.changeTrainersFirstName(trainer.getId(), firstName, token);
        gymFacade.changeTrainersLastName(trainer.getId(), lastName, token);
        gymFacade.changeTrainersUsername(trainer.getId(), newUsername, token);

        return Map.of("username", user.getUsername(),"firstName", user.getFirstName(), "lastName", user.getLastName(),
                "specialization", trainer.getTrainingType().getId(), "isActive", user.getIsActive(),
                "traineesList", trainer.getTrainees().stream()
                        .map(t -> Map.of("username", t.getUser().getUsername(), "firstName", t.getUser().getFirstName(),
                                "lastName", t.getUser().getLastName()))
                        .toList());
    }

    @GetMapping("/{username}/trainingsList")
    public List<Training> getTrainingsList(@PathVariable String username,
                                           @RequestParam(required = false) LocalDate fromDate,
                                           @RequestParam(required = false) LocalDate toDate,
                                           @RequestParam(required = false) String traineeName,
                                           @RequestHeader(name = "username") String usernameAuth,
                                           @RequestHeader(name = "password") String password) throws InstanceNotFoundException, CredentialNotFoundException {
        String token = gymFacade.authenticate(usernameAuth, password);

        return gymFacade.getTrainerTrainingsByUsernameAndCriteria(username, fromDate, toDate, traineeName, token);
    }

    @PatchMapping("/{username}/change-isActive/{isActive}")
    public ResponseEntity<String> changeIsActive(@PathVariable String username, @PathVariable Boolean isActive,
                                                 @RequestHeader(name = "username") String usernameAuth, @RequestHeader(name = "password") String password) throws InstanceNotFoundException, CredentialNotFoundException {
        String token = gymFacade.authenticate(usernameAuth, password);

        Long id = gymFacade.getTrainerByUsername(username, token).getId();

        gymFacade.changeTrainersIsActive(id, isActive, token);

        return ResponseEntity.ok(String.format("isActive changed successfully for the trainer with the username: %s", username));
    }
}
