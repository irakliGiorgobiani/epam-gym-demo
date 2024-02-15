package com.epam.epamgymdemo.repository;

import com.epam.epamgymdemo.model.Trainee;
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
public class TraineeRepository {

    private final Map<Long, Trainee> trainees;
    private final ResourceLoader resourceLoader;

    public TraineeRepository() {
        this.trainees = new HashMap<>();
        this.resourceLoader = new DefaultResourceLoader();
    }

    @PostConstruct
    private void loadDataFromFile() {
        try {
            Resource resource = resourceLoader.getResource("classpath:data/trainees.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(",");
                Long traineeId = Long.parseLong(split[0]);
                LocalDate doB = LocalDate.parse(split[1]);
                String address = split[2];
                Long userId = Long.parseLong(split[3]);
                Trainee trainee = new Trainee(traineeId, doB, address, userId);
                trainees.put(traineeId, trainee);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Trainee getTrainee(Long id) {
        return trainees.get(id);
    }

    public List<Trainee> getALlTrainees() {
        return new ArrayList<>(trainees.values());
    }

    public void addTrainee(Trainee trainee) {
        trainees.put(trainee.getTraineeId(), trainee);
    }

    public void updateTrainee(Trainee trainee) {
        trainees.put(trainee.getTraineeId(), trainee);
    }

    public void removeTrainee(Long id) {
        trainees.remove(id);
    }

    public boolean containsTrainee(Long id) {
        return trainees.containsKey(id);
    }

    public boolean containsTrainee(Trainee trainee) {
        return trainees.containsValue(trainee);
    }
}
