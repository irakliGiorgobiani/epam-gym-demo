package com.epam.epamgymdemo.dao;

import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.repository.TrainingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Repository
@Slf4j
public class TrainingDao implements DataCreator<Training>, DataSelector<Training> {

    private final TrainingRepository trainingRepository;

    public TrainingDao(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    public Training get(Long id) throws InstanceNotFoundException {
        return trainingRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException(String.format("Training not found with the id: %d", id)));
    }

    @Override
    public List<Training> getAll() {
        return trainingRepository.findAll();
    }

    @Override
    public void create(Training training) {
        if (trainingRepository.existsById(training.getTrainingId())) {
            throw new DuplicateKeyException("Training with this id already exists");
        } else {
            trainingRepository.save(training);
            log.info("Training created successfully");
        }
    }
}
