package com.epam.epamgymdemo.repository.DAO.classes;

import com.epam.epamgymdemo.repository.DAO.interfaces.CSUDAO;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.repository.storage.TrainerRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TrainerDAO implements CSUDAO<Trainer> {

    private final TrainerRepository trainerRepository;

    public TrainerDAO(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Override
    public Trainer get(String username) {
        return trainerRepository.getTrainers().get(username);
    }

    @Override
    public Map<String,Trainer> getAll() {
        return trainerRepository.getTrainers();
    }

    @Override
    public void create(Trainer trainer) {
        trainerRepository.getTrainers().put(trainer.getUserId(), trainer);
    }

    @Override
    public void update(Trainer trainer) {
        trainerRepository.getTrainers().put(trainer.getUserId(), trainer);
    }
}
