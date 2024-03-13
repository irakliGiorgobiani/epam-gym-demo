package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.model.bo.TrainingType;
import com.epam.epamgymdemo.repository.TrainingTypeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;

    public TrainingType getById(Long id) throws InstanceNotFoundException {
        return trainingTypeRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException(String.format("Training type not found with the id: %d", id)));
    }

    public List<TrainingType> getAll() {
        return trainingTypeRepository.findAll();
    }

    public TrainingType getTrainingTypeByName(String trainingType) throws InstanceNotFoundException {
        return trainingTypeRepository.findByTypeName(trainingType)
                .orElseThrow(() -> new InstanceNotFoundException(String.format("Training type instance not found with the name: %s", trainingType)));
    }
}
