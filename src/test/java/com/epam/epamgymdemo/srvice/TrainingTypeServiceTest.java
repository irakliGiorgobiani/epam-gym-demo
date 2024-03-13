package com.epam.epamgymdemo.srvice;

import com.epam.epamgymdemo.model.bo.TrainingType;
import com.epam.epamgymdemo.repository.TrainingTypeRepository;
import com.epam.epamgymdemo.service.TrainingTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeServiceTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeService trainingTypeService;

    @Test
    public void testGetById() throws InstanceNotFoundException {
        long id = 1L;

        TrainingType trainingType = TrainingType.builder()
                .id(id)
                .build();

        when(trainingTypeRepository.findById(id)).thenReturn(Optional.of(trainingType));

        TrainingType result = trainingTypeService.getById(id);

        assertEquals(trainingType, result);
    }

    @Test
    public void testGetAll() {
        List<TrainingType> trainingTypes = new ArrayList<>();
        trainingTypes.add(new TrainingType());
        trainingTypes.add(new TrainingType());
        trainingTypes.add(new TrainingType());

        when(trainingTypeRepository.findAll()).thenReturn(trainingTypes);

        List<TrainingType> result = trainingTypeService.getAll();

        assertEquals(trainingTypes, result);
    }

    @Test
    void testGetTrainingTypeByName() throws InstanceNotFoundException {
        String typeName = "yoga";

        TrainingType trainingType = TrainingType.builder()
                .typeName(typeName)
                .build();

        when(trainingTypeRepository.findByTypeName(typeName)).thenReturn(Optional.ofNullable(trainingType));

        TrainingType foundTrainingType = trainingTypeService.getTrainingTypeByName(typeName);

        assertEquals(trainingType, foundTrainingType);
    }
}
