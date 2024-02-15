package com.epam.epamgymdemo;

import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.model.TrainingType;
import com.epam.epamgymdemo.repository.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrainingRepositoryTest {

    private TrainingRepository trainingRepository;

    @BeforeEach
    void setUp() {
        trainingRepository = new TrainingRepository();
    }

    @Test
    void testGetTraining() {
        Training training = new Training(1L, 1L, 1L, "Training 1", TrainingType.YOGA, LocalDate.now(), 60);
        trainingRepository.addTraining(training);

        assertEquals(training, trainingRepository.getTraining(1L));
    }

    @Test
    void testGetAllTrainings() {
        Training training1 = new Training(1L, 1L, 1L, "Training 1", TrainingType.YOGA, LocalDate.now(), 60);
        Training training2 = new Training(2L, 2L, 2L, "Training 2", TrainingType.ZUMBA, LocalDate.now(), 45);

        trainingRepository.addTraining(training1);
        trainingRepository.addTraining(training2);

        List<Training> allTrainings = trainingRepository.getAllTrainings();

        assertTrue(allTrainings.contains(training1));
        assertTrue(allTrainings.contains(training2));
        assertEquals(2, allTrainings.size());
    }

    @Test
    void testAddTraining() {
        Training training = new Training(1L, 1L, 1L, "Training 1", TrainingType.YOGA, LocalDate.now(), 60);
        trainingRepository.addTraining(training);

        assertTrue(trainingRepository.containsTraining(1L));
    }

    @Test
    void testContainsTrainingById() {
        Training training = new Training(1L, 1L, 1L, "Training 1", TrainingType.YOGA, LocalDate.now(), 60);
        trainingRepository.addTraining(training);

        assertTrue(trainingRepository.containsTraining(1L));
    }

    @Test
    void testContainsTrainingByObject() {
        Training training = new Training(1L, 1L, 1L, "Training 1", TrainingType.YOGA, LocalDate.now(), 60);
        trainingRepository.addTraining(training);

        assertTrue(trainingRepository.containsTraining(training));
    }
}
