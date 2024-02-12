package com.epam.epamgymdemo.model;

import com.epam.epamgymdemo.model.enums.TrainingType;

import java.time.LocalDate;

public class Training {
    private String traineeId;
    private String trainerId;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDate trainingDate;
    private int trainingDuration;

    public Training(String traineeId, String trainerId, String trainingName, TrainingType trainingType, LocalDate trainingDate, int trainingDuration) {
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    public String getTraineeId() {
        return traineeId;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public int getTrainingDuration() {
        return trainingDuration;
    }
}
