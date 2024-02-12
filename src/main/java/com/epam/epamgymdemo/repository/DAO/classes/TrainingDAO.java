package com.epam.epamgymdemo.repository.DAO.classes;

import com.epam.epamgymdemo.repository.DAO.interfaces.CSDAO;
import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.repository.storage.TrainingRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
@Repository
public class TrainingDAO implements CSDAO<Training> {

    private final TrainingRepository trainingRepository;

    public TrainingDAO(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    public Training get(String username) {
        return trainingRepository.getTrainings().get(username);
    }

    @Override
    public Map<String, Training> getAll() {
        return trainingRepository.getTrainings();
    }

    @Override
    public void create(Training training) {
        trainingRepository.getTrainings().put(training.getTrainingName(), training);
    }
}
