package com.epam.epamgymdemo.repository;

import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.model.TrainingType;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TrainingRepository {

    private final Map<Long, Training> trainings;
    private final ResourceLoader resourceLoader;

    public TrainingRepository() {
        this.trainings = new HashMap<>();
        this.resourceLoader = new DefaultResourceLoader();
    }

    @PostConstruct
    private void loadDataFromFile() {
        try {
            Resource resource = resourceLoader.getResource("classpath:data/training.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(",");
                Long trainingId = Long.parseLong(split[0]);
                Long traineeId = Long.parseLong(split[1]);
                Long trainerId = Long.parseLong(split[2]);
                String trainingName = split[3];
                TrainingType trainingType = TrainingType.valueOf(split[4]);
                LocalDate trainingDate = LocalDate.parse(split[5]);
                Integer trainingDuration = Integer.parseInt(split[6]);
                Training training = new Training(trainingId, traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);
                trainings.put(trainingId, training);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Training getTraining(Long id) {
        return trainings.get(id);
    }

    public List<Training> getAllTrainings() {
        return new ArrayList<>(trainings.values());
    }

    public void addTraining(Training training) {
        trainings.put(training.getTrainingId(), training);
    }

    public boolean containsTraining(Long id) {
        return trainings.containsKey(id);
    }

    public boolean containsTraining(Training training) {
        return trainings.containsValue(training);
    }
}
