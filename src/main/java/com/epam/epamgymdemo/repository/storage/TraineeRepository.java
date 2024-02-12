package com.epam.epamgymdemo.repository.storage;

import com.epam.epamgymdemo.model.Trainee;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
@Repository
public class TraineeRepository {

    private final Map<String, Trainee> trainees;

    public TraineeRepository() {
        this.trainees = new HashMap<>();
    }

    public Map<String, Trainee> getTrainees() {
        return trainees;
    }
}
