package com.epam.epamgymdemo.dao;

import com.epam.epamgymdemo.model.Trainee;
import com.epam.epamgymdemo.repository.TraineeRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Repository
public class TraineeDao implements DataCreator<Trainee>, DataSelector<Trainee>, DataUpdater<Trainee>, DataDeleter {

    private final TraineeRepository traineeRepository;

    public TraineeDao(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    @Override
    public Trainee get(Long id) throws InstanceNotFoundException {
        return traineeRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException(String.format("Trainee not found with the id: %d", id)));
    }

    @Override
    public List<Trainee> getAll() {
        return traineeRepository.findAll();
    }

    @Override
    @Transactional
    public void create(Trainee trainee) {
        traineeRepository.save(trainee);
    }

    @Override
    @Transactional
    public void update(Trainee trainee) throws InstanceNotFoundException {
        if (!traineeRepository.existsById(trainee.getId())) {
            throw new InstanceNotFoundException(String.format("Trainee not found with id: %d", trainee.getId()));
        } else {
            traineeRepository.save(trainee);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) throws InstanceNotFoundException {
        if (!traineeRepository.existsById(id)) {
            throw new InstanceNotFoundException(String.format("Trainee not found with the id: %d", id));
        } else {
            traineeRepository.deleteById(id);
        }
    }
}
