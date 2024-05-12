package com.epam.epamgymdemo.metrics;

import com.epam.epamgymdemo.repository.TraineeRepository;
import com.epam.epamgymdemo.repository.TrainerRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class CustomMetrics {

    private final Counter traineeCounter;

    private final Counter trainerCounter;

    private final TraineeRepository traineeRepository;

    private final TrainerRepository trainerRepository;

    public CustomMetrics(PrometheusMeterRegistry prometheusMeterRegistry,
                         TraineeRepository traineeRepository, TrainerRepository trainerRepository) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.traineeCounter = Counter.builder("traineeCounter")
                .description("Total number of trainees")
                .register(prometheusMeterRegistry);

        this.trainerCounter = Counter.builder("trainerCounter")
                .description("Total number of trainers")
                .register(prometheusMeterRegistry);
    }

    @PostConstruct
    public void init() {
        long initialTraineeCount = traineeRepository.count();
        long initialTrainerCount = trainerRepository.count();

        this.traineeCounter.increment(initialTraineeCount);
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
