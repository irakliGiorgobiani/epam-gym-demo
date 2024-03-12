package com.epam.epamgymdemo.controller;

import com.epam.epamgymdemo.facade.GymFacade;
import com.epam.epamgymdemo.model.TrainingType;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.InstanceNotFoundException;
import javax.security.auth.login.CredentialNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/trainingTypes")
public class TrainingTypeController {

    private final GymFacade gymFacade;

    @GetMapping
    public Map<Long, String> getAll(@RequestHeader(name = "username") String username, @RequestHeader(name = "password") String password) throws InstanceNotFoundException, CredentialNotFoundException {
        String token = gymFacade.authenticate(username, password);

        List<TrainingType> trainingTypes = gymFacade.getAllTrainingTypes(token);

        Map<Long, String> result = new HashMap<>();

        for (TrainingType trainingType : trainingTypes) {
            result.put(trainingType.getId(), trainingType.getTypeName());
        }

        return result;
    }
}
