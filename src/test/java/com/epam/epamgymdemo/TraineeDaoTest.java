package com.epam.epamgymdemo;

import com.epam.epamgymdemo.Dao.TraineeDao;
import com.epam.epamgymdemo.model.Trainee;
import com.epam.epamgymdemo.repository.TraineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TraineeDaoTest {

    private TraineeDao traineeDao;

    @Mock
    private TraineeRepository traineeRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        traineeDao = new TraineeDao(traineeRepository);
    }

    @Test
    public void testGetTrainee() throws InstanceNotFoundException {
        Trainee trainee = new Trainee(1L, LocalDate.of(2000, 1, 1), "Address", 1L);
        when(traineeRepository.getTrainee(1L)).thenReturn(trainee);

        Trainee selectedTrainee = traineeDao.get(1L);
        assertEquals(trainee, selectedTrainee);
    }

    @Test
    public void testGetAllTrainees() {
        List<Trainee> trainees = new ArrayList<>();
        trainees.add(new Trainee(1L, LocalDate.of(2000, 1, 1), "Address1", 1L));
        trainees.add(new Trainee(2L, LocalDate.of(2000, 1, 2), "Address2", 2L));
        when(traineeRepository.getALlTrainees()).thenReturn(trainees);

        List<Trainee> selectedTrainees = traineeDao.getAll();
        assertEquals(trainees, selectedTrainees);
    }

    @Test
    public void testCreateTrainee() {
        Trainee trainee = new Trainee(1L, LocalDate.of(2000, 1, 1), "Address", 1L);
        traineeDao.create(trainee);
        verify(traineeRepository, times(1)).addTrainee(trainee);
    }

    @Test
    public void testUpdateTrainee() throws InstanceNotFoundException {
        Trainee trainee = new Trainee(1L, LocalDate.of(2000, 1, 1), "Address", 1L);
        when(traineeRepository.containsTrainee(trainee.getTraineeId())).thenReturn(true);

        traineeDao.update(trainee);
        verify(traineeRepository, times(1)).updateTrainee(trainee);
    }

    @Test
    public void testDeleteTrainee() throws InstanceNotFoundException {
        Trainee trainee = new Trainee(1L, LocalDate.of(2000, 1, 1), "Address", 1L);
        when(traineeRepository.getTrainee(1L)).thenReturn(trainee);

        traineeDao.delete(1L);
        verify(traineeRepository, times(1)).removeTrainee(1L);
    }
}

