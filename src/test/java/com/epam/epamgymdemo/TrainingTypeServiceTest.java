package com.epam.epamgymdemo;

import com.epam.epamgymdemo.model.TrainingType;
import com.epam.epamgymdemo.repository.TrainingTypeRepository;
import com.epam.epamgymdemo.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TrainingTypeServiceTest {
    @Mock
    private TrainingTypeRepository trainingTypeRepository;
    @InjectMocks
    private TrainingTypeService trainingTypeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetById() throws InstanceNotFoundException {
        long id = 1L;
        TrainingType trainingType = TrainingType.builder()
                .id(id)
                .build();

        when(trainingTypeRepository.findById(id)).thenReturn(Optional.of(trainingType));

        TrainingType result = trainingTypeService.getById(id);

        assertEquals(id, result.getId());
    }

    @Test
    public void testGetAll() {
        List<TrainingType> trainingTypes = new ArrayList<>();
        trainingTypes.add(new TrainingType());
        when(trainingTypeRepository.findAll()).thenReturn(trainingTypes);

        List<TrainingType> result = trainingTypeService.getAll();

        assertEquals(trainingTypes.size(), result.size());
    }
}
