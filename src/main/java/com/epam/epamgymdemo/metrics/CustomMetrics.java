package com.epam.epamgymdemo.metrics;

import com.epam.epamgymdemo.repository.TraineeRepository;
import com.epam.epamgymdemo.repository.TrainerRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CustomMetrics {

    private final Counter traineeCounter;

    private final Counter trainerCounter;

    public CustomMetrics(PrometheusMeterRegistry prometheusMeterRegistry, TraineeRepository traineeRepository,
                         TrainerRepository trainerRepository) {
        long initialTraineeCount = traineeRepository.count();
        long initialTrainerCount = trainerRepository.count();

        this.traineeCounter = Counter.builder("traineeCounter")
                .description("Total number of trainees")
                .register(prometheusMeterRegistry);
        this.traineeCounter.increment(initialTraineeCount);

        this.trainerCounter = Counter.builder("trainerCounter")
                .description("Total number of trainers")
                .register(prometheusMeterRegistry);
        this.trainerCounter.increment(initialTrainerCount);
    }

    public void incrementTraineeCounter() {
        traineeCounter.increment();
    }

    public void decrementTraineeCounter() {
        traineeCounter.increment(-1);
    }

    public void incrementTrainerCounter() {
        trainerCounter.increment();
    }
}
