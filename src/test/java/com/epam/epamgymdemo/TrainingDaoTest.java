package com.epam.epamgymdemo;

import com.epam.epamgymdemo.dao.TrainingDao;
import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.repository.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingDaoTest {

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TrainingDao trainingDao;

    private Training training;

    @BeforeEach
    public void setUp() {
        training = Training.builder()
                .id(1L)
                .trainingName("Test Training")
                .trainingDate(LocalDate.now())
                .trainingDuration(1)
                .build();
    }

    @Test
    public void testGetTrainingById() throws InstanceNotFoundException {
        when(trainingRepository.findById(1L)).thenReturn(Optional.of(training));

        Training result = trainingDao.get(1L);

        assertEquals(training, result);
    }

    @Test
    public void testGetTrainingByIdNotFound() {
        when(trainingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InstanceNotFoundException.class, () -> trainingDao.get(1L));
    }

    @Test
    public void testGetAllTrainings() {
        List<Training> trainingList = new ArrayList<>();
        trainingList.add(training);
        when(trainingRepository.findAll()).thenReturn(trainingList);

        List<Training> result = trainingDao.getAll();

        assertEquals(trainingList, result);
    }

    @Test
    public void testCreateTraining() {
        when(trainingRepository.existsById(1L)).thenReturn(false);

        trainingDao.create(training);

        verify(trainingRepository, times(1)).save(training);
    }

    @Test
    public void testCreateTrainingAlreadyExists() {
        when(trainingRepository.existsById(1L)).thenReturn(true);

        assertThrows(DuplicateKeyException.class, () -> trainingDao.create(training));
    }
}
