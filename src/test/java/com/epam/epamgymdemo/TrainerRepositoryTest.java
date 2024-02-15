package com.epam.epamgymdemo;

import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.TrainingType;
import com.epam.epamgymdemo.repository.TrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrainerRepositoryTest {

    private TrainerRepository trainerRepository;

    @BeforeEach
    void setUp() {
        trainerRepository = new TrainerRepository();
    }

    @Test
    void testGetTrainer() {
        Trainer trainer = new Trainer(1L, TrainingType.YOGA, 1L);
        trainerRepository.addTrainer(trainer);

        assertEquals(trainer, trainerRepository.getTrainer(1L));
    }

    @Test
    void testGetAllTrainers() {
        Trainer trainer1 = new Trainer(1L, TrainingType.YOGA, 1L);
        Trainer trainer2 = new Trainer(2L, TrainingType.ZUMBA, 2L);

        trainerRepository.addTrainer(trainer1);
        trainerRepository.addTrainer(trainer2);

        List<Trainer> allTrainers = trainerRepository.getAllTrainers();

        assertTrue(allTrainers.contains(trainer1));
        assertTrue(allTrainers.contains(trainer2));
        assertEquals(2, allTrainers.size());
    }

    @Test
    void testAddTrainer() {
        Trainer trainer = new Trainer(1L, TrainingType.YOGA, 1L);
        trainerRepository.addTrainer(trainer);

        assertTrue(trainerRepository.containsTrainer(1L));
    }

    @Test
    void testUpdateTrainer() {
        Trainer trainer = new Trainer(1L, TrainingType.YOGA, 1L);
        trainerRepository.addTrainer(trainer);

        Trainer updatedTrainer = new Trainer(1L, TrainingType.ZUMBA, 1L);
        trainerRepository.updateTrainer(updatedTrainer);

        assertEquals(updatedTrainer, trainerRepository.getTrainer(1L));
    }

    @Test
    void testContainsTrainerById() {
        Trainer trainer = new Trainer(1L, TrainingType.YOGA, 1L);
        trainerRepository.addTrainer(trainer);

        assertTrue(trainerRepository.containsTrainer(1L));
    }

    @Test
    void testContainsTrainerByObject() {
        Trainer trainer = new Trainer(1L, TrainingType.YOGA, 1L);
        trainerRepository.addTrainer(trainer);

        assertTrue(trainerRepository.containsTrainer(trainer));
    }
}
