package com.epam.epamgymdemo.repository.DAO.classes;

import com.epam.epamgymdemo.repository.DAO.interfaces.CSUDDAO;
import com.epam.epamgymdemo.model.Trainee;
import com.epam.epamgymdemo.repository.storage.TraineeRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
@Repository
public class TraineeDAO implements CSUDDAO<Trainee> {

    private final TraineeRepository traineeRepository;

    public TraineeDAO(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    @Override
    public Trainee get(String username) {
        return traineeRepository.getTrainees().get(username);
    }

    @Override
    public Map<String, Trainee> getAll() {
        return traineeRepository.getTrainees();
    }

    @Override
    public void create(Trainee trainee) {
        traineeRepository.getTrainees().put(trainee.getUserId(), trainee);
    }

    @Override
    public void update(Trainee trainee) {
        traineeRepository.getTrainees().put(trainee.getUserId(), trainee);
    }

    @Override
    public void delete(String username) {
        traineeRepository.getTrainees().remove(username);
    }
}
