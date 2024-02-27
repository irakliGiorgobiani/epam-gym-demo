package com.epam.epamgymdemo.dao;

import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.repository.TrainerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Repository
@Slf4j
public class TrainerDao implements DataCreator<Trainer>, DataSelector<Trainer>, DataUpdater<Trainer> {

    private final TrainerRepository trainerRepository;

    public TrainerDao(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Override
    public Trainer get(Long id) throws InstanceNotFoundException {
        return trainerRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException(String.format("Trainer not found with the id: %d", id)));
    }

    @Override
    public List<Trainer> getAll() {
        return trainerRepository.findAll();
    }

    @Override
    @Transactional
    public void create(Trainer trainer) {
        if (trainerRepository.existsById(trainer.getId())) {
            throw new DuplicateKeyException(String.format("Trainer with the id: %d already exists", trainer.getId()));
        } else {
            trainerRepository.save(trainer);
        }
    }

    @Override
    @Transactional
    public void update(Trainer trainer) throws InstanceNotFoundException {
        if (!trainerRepository.existsById(trainer.getId())) {
            throw new InstanceNotFoundException(String.format("Trainer not found with the id: %d", trainer.getId()));
        } else {
            trainerRepository.save(trainer);
        }
    }
}
