package com.epam.epamgymdemo;

import com.epam.epamgymdemo.dao.TrainingTypeDao;
import com.epam.epamgymdemo.model.TrainingType;
import com.epam.epamgymdemo.repository.TrainingTypeRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeDaoTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeDao trainingTypeDao;

    @Test
    public void testGetTrainingTypeById() throws InstanceNotFoundException {
        TrainingType trainingType = TrainingType.builder()
                .id(1L)
                .typeName("Test Type")
                .build();
        when(trainingTypeRepository.findById(1L)).thenReturn(Optional.of(trainingType));

        TrainingType result = trainingTypeDao.get(1L);

        assertEquals(trainingType, result);
    }

    @Test
    public void testGetTrainingTypeByIdNotFound() {
        when(trainingTypeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InstanceNotFoundException.class, () -> trainingTypeDao.get(1L));
    }

    @Test
    public void testGetAllTrainingTypes() {
        List<TrainingType> trainingTypes = new ArrayList<>();
        trainingTypes.add(TrainingType.builder()
                .id(1L)
                .typeName("Test Type 1")
                .build());
        trainingTypes.add(TrainingType.builder()
                .id(2L)
                .typeName("Test Type 2")
                .build());
        when(trainingTypeRepository.findAll()).thenReturn(trainingTypes);

        List<TrainingType> result = trainingTypeDao.getAll();

        assertEquals(trainingTypes, result);
    }
}

