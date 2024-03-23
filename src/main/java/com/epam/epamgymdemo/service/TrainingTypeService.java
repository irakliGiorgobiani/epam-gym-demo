package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.exception.EntityNotFoundException;
import com.epam.epamgymdemo.model.bo.TrainingType;
import com.epam.epamgymdemo.model.dto.TrainingTypeDto;
import com.epam.epamgymdemo.repository.TrainingTypeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;

    public List<TrainingTypeDto> getAll() {
        List<TrainingType> trainingTypes = trainingTypeRepository.findAll();

        List<TrainingTypeDto> trainingTypeDtoList = new ArrayList<>();

        for (TrainingType trainingType : trainingTypes) {
            trainingTypeDtoList.add(TrainingTypeDto.builder()
                    .typeName(trainingType.getTypeName())
                    .id(trainingType.getId())
                    .build());
        }

        return trainingTypeDtoList;
    }

    public TrainingType getTrainingTypeByName(String trainingType) {
        if (trainingType != null) {
            return trainingTypeRepository.findByTypeName(trainingType)
                    .orElseThrow(() -> new EntityNotFoundException
                            (String.format("Training type instance not found with the name: %s", trainingType)));
        } else return null;
    }
}