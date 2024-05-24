package com.epam.epamgymdemo.epamgymreporter.feignclient;

import com.epam.epamgymdemo.config.FeignConfig;
import com.epam.epamgymdemo.model.dto.ReporterTrainingDto;
import com.epam.epamgymdemo.model.dto.TrainingSummaryDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "epam-gym-reporter", configuration = FeignConfig.class)
@CircuitBreaker(name = "trainingSummary")
public interface ReporterClient {

    @PostMapping("/training-summary/v1")
    ResponseEntity<String> saveTraining(@RequestBody ReporterTrainingDto reporterTrainingDto);

    @GetMapping("/training-summary/v1/{username}")
    TrainingSummaryDto getMonthlyTrainingSummary(@PathVariable("username") String username);
}
