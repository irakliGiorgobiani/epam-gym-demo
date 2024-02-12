package com.epam.epamgymdemo.repository.storage;

import com.epam.epamgymdemo.model.Trainer;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class TrainerRepository {

    private final Map<String, Trainer> trainers;

    public TrainerRepository() {
        this.trainers = new HashMap<>();
    }

    public Map<String, Trainer> getTrainers() {
        return trainers;
    }
}
