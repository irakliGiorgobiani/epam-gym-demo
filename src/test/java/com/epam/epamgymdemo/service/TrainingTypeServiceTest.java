package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.model.bo.TrainingType;
import com.epam.epamgymdemo.model.dto.TrainingTypeDto;
import com.epam.epamgymdemo.repository.TrainingTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TrainingTypeServiceTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeService trainingTypeService;

    @Test
    public void testGetAll() {
        List<TrainingType> trainingTypes = new ArrayList<>();
        trainingTypes.add(new TrainingType(1L, "Type1"));
        trainingTypes.add(new TrainingType(2L, "Type2"));
        when(trainingTypeRepository.findAll()).thenReturn(trainingTypes);

        List<TrainingTypeDto> result = trainingTypeService.getAll();

        assertEquals(2, result.size());
        assertEquals("Type1", result.get(0).getTypeName());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Type2", result.get(1).getTypeName());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void testGetTrainingTypeByName() {
        String typeName = "yoga";

        TrainingType trainingType = TrainingType.builder()
                .typeName(typeName)
                .build();

        when(trainingTypeRepository.findByTypeName(typeName)).thenReturn(Optional.ofNullable(trainingType));

        TrainingType foundTrainingType = trainingTypeService.getTrainingTypeByName(typeName);

        assertEquals(trainingType, foundTrainingType);
    }
}
