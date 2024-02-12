package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.repository.DAO.classes.TrainerDAO;
import org.springframework.stereotype.Service;

@Service
public class TrainerService {

    private final TrainerDAO trainerDAO;

    public TrainerService(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    public void createTrainer() {
        return;
    }

    public void updateTrainer() {
        return;
    }

    public void selectTrainer() {
        return;
    }
}
