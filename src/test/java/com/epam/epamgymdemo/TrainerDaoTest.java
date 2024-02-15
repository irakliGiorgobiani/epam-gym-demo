package com.epam.epamgymdemo;

import com.epam.epamgymdemo.Dao.TrainerDao;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.TrainingType;
import com.epam.epamgymdemo.repository.TrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TrainerDaoTest {

    private TrainerDao trainerDao;

    @Mock
    private TrainerRepository trainerRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        trainerDao = new TrainerDao(trainerRepository);
    }

    @Test
    public void testGetTrainer() throws InstanceNotFoundException {
        Trainer trainer = new Trainer(1L, TrainingType.YOGA, 1L);
        when(trainerRepository.getTrainer(1L)).thenReturn(trainer);

        Trainer selectedTrainer = trainerDao.get(1L);
        assertEquals(trainer, selectedTrainer);
    }

    @Test
    public void testGetAllTrainers() {
        List<Trainer> trainers = new ArrayList<>();
        trainers.add(new Trainer(1L, TrainingType.YOGA, 1L));
        trainers.add(new Trainer(2L, TrainingType.ZUMBA, 2L));
        when(trainerRepository.getAllTrainers()).thenReturn(trainers);

        List<Trainer> selectedTrainers = trainerDao.getAll();
        assertEquals(trainers, selectedTrainers);
    }

    @Test
    public void testCreateTrainer() {
        Trainer trainer = new Trainer(1L, TrainingType.YOGA, 1L);
        trainerDao.create(trainer);
        verify(trainerRepository, times(1)).addTrainer(trainer);
    }

    @Test
    public void testUpdateTrainer() throws InstanceNotFoundException {
        Trainer trainer = new Trainer(1L, TrainingType.YOGA, 1L);
        when(trainerRepository.containsTrainer(trainer.getTrainerId())).thenReturn(true);

        trainerDao.update(trainer);
        verify(trainerRepository, times(1)).updateTrainer(trainer);
    }
}

