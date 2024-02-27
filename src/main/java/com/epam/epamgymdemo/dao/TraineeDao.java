package com.epam.epamgymdemo.dao;

import com.epam.epamgymdemo.model.Trainee;
import com.epam.epamgymdemo.repository.TraineeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Repository
@Slf4j
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
    public void create(Trainee trainee) {
        if (traineeRepository.existsById(trainee.getTraineeId())) {
            throw new DuplicateKeyException("Trainee with this id already exists");
        } else {
            traineeRepository.save(trainee);
            log.info("Trainee successfully created");
        }
    }

    @Override
    public void update(Trainee trainee) throws InstanceNotFoundException {
        if (!traineeRepository.existsById(trainee.getTraineeId())) {
            throw new InstanceNotFoundException(String.format("Trainee not found with id: %d", trainee.getTraineeId()));
        } else {
            traineeRepository.save(trainee);
            log.info("Trainee successfully updated");
        }
    }

    @Override
    public void delete(Long id) throws InstanceNotFoundException {
        if (!traineeRepository.existsById(id)) {
            throw new InstanceNotFoundException(String.format("Trainee not found with the id: %d", id));
        } else {
            traineeRepository.deleteById(id);
            log.info("Trainee successfully deleted");
        }
    }
}
