package com.epam.epamgymdemo.controller;

import com.epam.epamgymdemo.model.bo.Training;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;
import javax.security.auth.login.CredentialNotFoundException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/trainings")
public class TrainingController {

    private final GymFacade gymFacade;

    @PostMapping("/add")
    public ResponseEntity<String> addTraining(@RequestBody Map<String, String> requestBody,
                                              @RequestHeader(name = "username") String usernameAuth,
                                              @RequestHeader(name = "password") String password) throws InstanceNotFoundException, CredentialNotFoundException, ParseException {
        String token = gymFacade.authenticate(usernameAuth, password);

        String traineeUsername = requestBody.get("traineeUsername");
        String trainerUsername = requestBody.get("trainerUsername");
        String trainingName = requestBody.get("trainingName");
        String trainingTypeName = requestBody.get("trainingTypeName");
        LocalDate trainingDate = LocalDate.parse(requestBody.get("trainingDate"));

        NumberFormat numberFormat = NumberFormat.getInstance();
        Number trainingDuration = numberFormat.parse(requestBody.get("trainingDuration"));

        Long traineeId = gymFacade.getTraineeByUsername(traineeUsername, token).getId();
        Long trainerId = gymFacade.getTrainerByUsername(trainerUsername, token).getId();
        Long typeId = gymFacade.getTrainingTypeByName(trainingTypeName, token).getId();

        Training training = gymFacade.createTraining(trainingName, trainingDate, trainingDuration, traineeId, trainerId, typeId, token);

        return ResponseEntity.ok(String.format("Training added successfully with the id: %d", training.getId()));
    }
}
