package com.epam.epamgymdemo.controller;

import com.epam.epamgymdemo.model.dto.TrainingTypeDto;
import com.epam.epamgymdemo.service.TrainingTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/training-type/v1")
public class TrainingTypeController {

    private final TrainingTypeService trainingTypeService;

    @GetMapping
    @Operation(summary = "Retrieving all trainingTypes",
            description = "Get all training types that are available from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all trainingTypes"),
            @ApiResponse(responseCode = "401", description = "Invalid Username or password"),
    })
    public ResponseEntity<List<TrainingTypeDto>> getAll() {
        return ResponseEntity.ok(trainingTypeService.getAll());
    }
}