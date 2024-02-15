package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.Dao.TrainerDao;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.TrainingType;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Service
public class TrainerService {

    private final TrainerDao trainerDAO;

    public TrainerService(TrainerDao trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    public void createTrainer(Long trainerId, TrainingType specialization, Long userId) {
        trainerDAO.create(new Trainer(trainerId, specialization, userId));
    }

    public void updateTrainer(Long trainerId, TrainingType specialization, Long userId) throws InstanceNotFoundException {
        trainerDAO.update(new Trainer(trainerId, specialization, userId));
    }

    public Trainer selectTrainer(Long id) throws InstanceNotFoundException {
        return trainerDAO.get(id);
    }

    public List<Trainer> selectAllTrainers() {
        return trainerDAO.getAll();
    }
}
