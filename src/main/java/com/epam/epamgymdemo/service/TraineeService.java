package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.repository.DAO.classes.TraineeDAO;
import org.springframework.stereotype.Service;

@Service
public class TraineeService {

    private final TraineeDAO traineeDAO;

    public TraineeService(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    public void createTrainee() {
        return;
    }

    public void updateTrainee() {
        return;
    }

    public void selectTrainee() {
        return;
    }

    public void deleteTrainee() {
        return;
    }
}
