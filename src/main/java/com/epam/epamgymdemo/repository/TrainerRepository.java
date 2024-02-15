package com.epam.epamgymdemo.repository;

import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.TrainingType;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TrainerRepository {

    private final Map<Long, Trainer> trainers;
    private final ResourceLoader resourceLoader;

    public TrainerRepository() {
        this.trainers = new HashMap<>();
        this.resourceLoader = new DefaultResourceLoader();
    }

    @PostConstruct
    private void loadDataFromFile() {
        try {
            Resource resource = resourceLoader.getResource("classpath:data/trainers.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(",");
                Long trainerId = Long.parseLong(split[0]);
                TrainingType specialization = TrainingType.valueOf(split[1]);
                Long userId = Long.parseLong(split[2]);
                Trainer trainer = new Trainer(trainerId, specialization, userId);
                trainers.put(trainerId, trainer);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Trainer getTrainer(Long id) {
        return trainers.get(id);
    }

    public List<Trainer> getAllTrainers() {
        return new ArrayList<>(trainers.values());
    }

    public void addTrainer(Trainer trainee) {
        trainers.put(trainee.getTrainerId(), trainee);
    }

    public void updateTrainer(Trainer trainee) {
        trainers.put(trainee.getTrainerId(), trainee);
    }

    public boolean containsTrainer(Long id) {
        return trainers.containsKey(id);
    }

    public boolean containsTrainer(Trainer trainee) {
        return trainers.containsValue(trainee);
    }
}
