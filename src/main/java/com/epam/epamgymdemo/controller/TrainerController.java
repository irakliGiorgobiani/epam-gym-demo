package com.epam.epamgymdemo.controller;

import com.epam.epamgymdemo.facade.GymFacade;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.model.User;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trainer")
public class TrainerController {
    private final GymFacade gymFacade;

    public TrainerController(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> requestBody) throws InstanceNotFoundException {
        String firstName = requestBody.get("firstName");
        String lastName = requestBody.get("lastName");
        Long trainingType = Long.parseLong(requestBody.get("specialization"));

        Trainer trainer = gymFacade.createTrainer(trainingType, firstName, lastName, true);

        return Map.of("username", trainer.getUser().getUserName(),
                "password", trainer.getUser().getPassword());
    }

    @GetMapping("/{username}")
    public Map<String, Object> get(@PathVariable String username,
                                   @RequestHeader(name = "username") String usernameAuth, @RequestHeader String password) throws InstanceNotFoundException {
        Trainer trainer = gymFacade.selectTrainerByUsername(username, usernameAuth, password);
        User user = trainer.getUser();

        return Map.of("firstName", user.getFirstName(), "lastName", user.getLastName(),
                "specialization", trainer.getTrainingType().getId(), "isActive", user.getIsActive(),
                "traineesList", trainer.getTrainees().stream()
                        .map(t -> Map.of("username", t.getUser().getUserName(), "firstName", t.getUser().getFirstName(),
                                "lastName", t.getUser().getLastName()))
                        .toList());
    }

    @PutMapping("/update")
    public Map<String, Object> update(@RequestHeader(name = "username") String oldUsername, @RequestHeader String password,
                                      @RequestBody Map<String, String> requestBody) throws InstanceNotFoundException {
        String newUsername = requestBody.get("username");
        String firstName = requestBody.get("firstName");
        String lastName = requestBody.get("lastName");
        Long trainingType = Long.parseLong(requestBody.get("specialization"));
        Boolean isActive = Boolean.parseBoolean(requestBody.get("isActive"));

        Trainer trainer = gymFacade.selectTrainerByUsername(oldUsername, oldUsername, password);
        User user = trainer.getUser();

        gymFacade.updateTrainer(trainer.getId(), trainer.getTrainingType().getId(), user.getId(), oldUsername, password);
        gymFacade.changeTrainersIsActive(trainer.getId(), isActive, oldUsername, password);
        gymFacade.changeTrainersFirstName(trainer.getId(), firstName, oldUsername, password);
        gymFacade.changeTrainersLastName(trainer.getId(), lastName, oldUsername, password);
        gymFacade.changeTrainersUsername(trainer.getId(), newUsername, oldUsername, password);

        return Map.of("username", user.getUserName(),"firstName", user.getFirstName(), "lastName", user.getLastName(),
                "specialization", trainer.getTrainingType().getId(), "isActive", user.getIsActive(),
                "traineesList", trainer.getTrainees().stream()
                        .map(t -> Map.of("username", t.getUser().getUserName(), "firstName", t.getUser().getFirstName(),
                                "lastName", t.getUser().getLastName()))
                        .toList());
    }

    @GetMapping("/{username}/trainingsList")
    public List<Training> getTrainingsList(@PathVariable String username,
                                           @RequestParam(required = false) LocalDate fromDate,
                                           @RequestParam(required = false) LocalDate toDate,
                                           @RequestParam(required = false) String traineeName,
                                           @RequestHeader(name = "username") String usernameAuth,
                                           @RequestHeader(name = "password") String password) throws InstanceNotFoundException {
        return gymFacade.selectTrainerTrainingsByUsernameAndCriteria(username, fromDate, toDate, traineeName, usernameAuth, password);
    }
}
