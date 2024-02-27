package com.epam.epamgymdemo;

import static org.assertj.core.api.Assertions.assertThat;

import com.epam.epamgymdemo.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DatabaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    @Test
    public void testDatabaseConnectivity() {
        assertThat(userRepository).isNotNull();
        assertThat(traineeRepository).isNotNull();
        assertThat(trainerRepository).isNotNull();
        assertThat(trainingTypeRepository).isNotNull();
        assertThat(trainingRepository).isNotNull();
    }
}

