package com.epam.epamgymdemo;

import com.epam.epamgymdemo.dao.TrainerDao;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.repository.TrainerRepository;
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
public class TrainerDaoTest {

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainerDao trainerDao;

    private Trainer trainer;

    @BeforeEach
    public void setUp() {
        trainer = Trainer.builder()
                .id(1L)
                .build();
    }

    @Test
    public void testGetTrainerById() throws InstanceNotFoundException {
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer));

        Trainer result = trainerDao.get(1L);

        assertEquals(trainer, result);
    }

    @Test
    public void testGetTrainerByIdNotFound() {
        when(trainerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InstanceNotFoundException.class, () -> trainerDao.get(1L));
    }

    @Test
    public void testGetAllTrainers() {
        List<Trainer> trainerList = new ArrayList<>();
        trainerList.add(trainer);
        when(trainerRepository.findAll()).thenReturn(trainerList);

        List<Trainer> result = trainerDao.getAll();

        assertEquals(trainerList, result);
    }

    @Test
    public void testCreateTrainer() {
        when(trainerRepository.existsById(1L)).thenReturn(false);

        trainerDao.create(trainer);

        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    public void testCreateTrainerAlreadyExists() {
        when(trainerRepository.existsById(1L)).thenReturn(true);

        assertThrows(DuplicateKeyException.class, () -> trainerDao.create(trainer));
    }

    @Test
    public void testUpdateTrainer() throws InstanceNotFoundException {
        when(trainerRepository.existsById(1L)).thenReturn(true);

        trainerDao.update(trainer);

        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    public void testUpdateTrainerNotFound() {
        when(trainerRepository.existsById(1L)).thenReturn(false);

        assertThrows(InstanceNotFoundException.class, () -> trainerDao.update(trainer));
    }
}
