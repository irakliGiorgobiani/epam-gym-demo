package com.epam.epamgymdemo.Dao;

import com.epam.epamgymdemo.logger.LoggerUtil;
import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.repository.TrainingRepository;
import org.springframework.stereotype.Repository;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Repository
public class TrainingDao implements DataCreator<Training>, DataSelector<Training> {

    private final TrainingRepository trainingRepository;

    public TrainingDao(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    public Training get(Long id) throws InstanceNotFoundException {
        if (trainingRepository.getTraining(id) == null) {
            throw new InstanceNotFoundException("Training not found with the given id: " + id);
        } else return trainingRepository.getTraining(id);
    }

    @Override
    public List<Training> getAll() {
        return trainingRepository.getAllTrainings();
    }

    @Override
    public void create(Training training) {
        trainingRepository.addTraining(training);
        LoggerUtil.getLogger().info("Training created successfully");
    }
}
