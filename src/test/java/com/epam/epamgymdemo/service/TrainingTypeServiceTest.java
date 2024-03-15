package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.model.bo.TrainingType;
import com.epam.epamgymdemo.repository.TrainingTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TrainingTypeServiceTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeService trainingTypeService;

    @Test
    public void testGetAll() {
        trainingTypeService.getAll();

        verify(trainingTypeRepository).findAll();
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
