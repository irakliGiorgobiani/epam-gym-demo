package com.epam.epamgymdemo.repository.storage;

import com.epam.epamgymdemo.model.Training;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class TrainingRepository {

    private final Map<String, Training> trainings;

    public TrainingRepository() {
        this.trainings = new HashMap<>();
    }

    public Map<String, Training> getTrainings() {
        return trainings;
    }
}
