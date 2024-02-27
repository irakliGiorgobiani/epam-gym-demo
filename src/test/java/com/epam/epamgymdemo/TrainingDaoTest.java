package com.epam.epamgymdemo;

import com.epam.epamgymdemo.dao.TrainingDao;
import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.repository.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TrainingDaoTest {

    @Mock
    private TrainingRepository trainingRepository;

    private TrainingDao trainingDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        trainingDao = new TrainingDao(trainingRepository);
    }

    @Test
    public void testGet() throws InstanceNotFoundException {
        Long trainingId = 1L;
        Training expectedTraining = new Training(trainingId, 1L, 1L, "Training", null, null, 0);
        when(trainingRepository.getTraining(trainingId)).thenReturn(expectedTraining);

        Training actualTraining = trainingDao.get(trainingId);

        assertEquals(expectedTraining, actualTraining);
    }

    @Test
    public void testGetAll() {
        List<Training> expectedTrainings = new ArrayList<>();
        expectedTrainings.add(new Training(1L, 1L, 1L, "Training 1", null, null, 0));
        expectedTrainings.add(new Training(2L, 2L, 2L, "Training 2", null, null, 0));
        when(trainingRepository.getAllTrainings()).thenReturn(expectedTrainings);

        List<Training> actualTrainings = trainingDao.getAll();

        assertEquals(expectedTrainings.size(), actualTrainings.size());
        for (int i = 0; i < expectedTrainings.size(); i++) {
            assertEquals(expectedTrainings.get(i), actualTrainings.get(i));
        }
    }

    @Test
    public void testCreate() {
        Training training = new Training(1L, 1L, 1L, "Training", null, null, 0);

        trainingDao.create(training);

        verify(trainingRepository, times(1)).addTraining(training);
    }
}

