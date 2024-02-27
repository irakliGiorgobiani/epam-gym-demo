package com.epam.epamgymdemo.dao;

import com.epam.epamgymdemo.model.TrainingType;
import com.epam.epamgymdemo.repository.TrainingTypeRepository;
import org.springframework.stereotype.Repository;

import javax.management.InstanceNotFoundException;
import java.util.List;
@Repository
public class TrainingTypeDao implements DataSelector<TrainingType> {

    private final TrainingTypeRepository trainingTypeRepository;

    public TrainingTypeDao(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    public TrainingType get(Long id) throws InstanceNotFoundException {
        return trainingTypeRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException(String.format("Training type not found with the id: %d", id)));
    }

    @Override
    public List<TrainingType> getAll() {
        return trainingTypeRepository.findAll();
    }
}
