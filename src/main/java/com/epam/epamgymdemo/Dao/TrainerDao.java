package com.epam.epamgymdemo.Dao;

import com.epam.epamgymdemo.logger.LoggerUtil;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.repository.TrainerRepository;
import org.springframework.stereotype.Repository;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Repository
public class TrainerDao implements DataCreator<Trainer>, DataSelector<Trainer>, DataUpdater<Trainer> {

    private final TrainerRepository trainerRepository;

    public TrainerDao(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Override
    public Trainer get(Long id) throws InstanceNotFoundException {
        if (trainerRepository.getTrainer(id) == null) {
            throw new InstanceNotFoundException("Trainer not found with the id: " + id);
        } else return trainerRepository.getTrainer(id);
    }

    @Override
    public List<Trainer> getAll() {
        return trainerRepository.getAllTrainers();
    }

    @Override
    public void create(Trainer trainer) {
        trainerRepository.addTrainer(trainer);
        LoggerUtil.getLogger().info("Trainer successfully created");
    }

    @Override
    public void update(Trainer trainer) throws InstanceNotFoundException {
        if (!trainerRepository.containsTrainer(trainer.getTrainerId())) {
            throw new InstanceNotFoundException("Trainer not found with the id: " + trainer.getTrainerId());
        } else trainerRepository.updateTrainer(trainer);
    }
}
