package com.epam.epamgymdemo;

import com.epam.epamgymdemo.dao.TraineeDao;
import com.epam.epamgymdemo.model.Trainee;
import com.epam.epamgymdemo.repository.TraineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TraineeDaoTest {

    @Mock
    private TraineeRepository traineeRepository;

    @InjectMocks
    private TraineeDao traineeDao;

    private Trainee trainee;

    @BeforeEach
    public void setUp() {
        trainee = Trainee.builder().id(1L).build();
    }

    @Test
    public void testGetTraineeById() throws InstanceNotFoundException {
        when(traineeRepository.findById(1L)).thenReturn(Optional.of(trainee));

        Trainee result = traineeDao.get(1L);

        assertEquals(trainee, result);
    }

    @Test
    public void testGetTraineeByIdNotFound() {
        when(traineeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InstanceNotFoundException.class, () -> traineeDao.get(1L));
    }

    @Test
    public void testGetAllTrainees() {
        List<Trainee> traineeList = new ArrayList<>();
        traineeList.add(trainee);
        when(traineeRepository.findAll()).thenReturn(traineeList);

        List<Trainee> result = traineeDao.getAll();

        assertEquals(traineeList, result);
    }

    @Test
    public void testCreateTrainee() {
        when(traineeRepository.existsById(1L)).thenReturn(false);

        traineeDao.create(trainee);

        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    public void testCreateTraineeAlreadyExists() {
        when(traineeRepository.existsById(1L)).thenReturn(true);

        assertThrows(DuplicateKeyException.class, () -> traineeDao.create(trainee));
    }

    @Test
    public void testUpdateTrainee() throws InstanceNotFoundException {
        when(traineeRepository.existsById(1L)).thenReturn(true);

        traineeDao.update(trainee);

        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    public void testUpdateTraineeNotFound() {
        when(traineeRepository.existsById(1L)).thenReturn(false);

        assertThrows(InstanceNotFoundException.class, () -> traineeDao.update(trainee));
    }

    @Test
    public void testDeleteTrainee() throws InstanceNotFoundException {
        when(traineeRepository.existsById(1L)).thenReturn(true);

        traineeDao.delete(1L);

        verify(traineeRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteTraineeNotFound() {
        when(traineeRepository.existsById(1L)).thenReturn(false);

        assertThrows(InstanceNotFoundException.class, () -> traineeDao.delete(1L));
    }
}
