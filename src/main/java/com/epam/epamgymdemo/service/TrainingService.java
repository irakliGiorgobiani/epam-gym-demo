package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.repository.DAO.classes.TrainingDAO;
import org.springframework.stereotype.Service;

@Service
public class TrainingService {

    private final TrainingDAO trainingDAO;

    public TrainingService(TrainingDAO trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    public void createTraining() {
        return;
    }

    public void selectTraining() {
        return;
    }
}
