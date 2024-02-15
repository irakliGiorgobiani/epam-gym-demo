package com.epam.epamgymdemo.Dao;

import com.epam.epamgymdemo.logger.LoggerUtil;
import com.epam.epamgymdemo.model.Trainee;
import com.epam.epamgymdemo.repository.TraineeRepository;
import org.springframework.stereotype.Repository;

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
        if (traineeRepository.getTrainee(id) == null) {
            throw new InstanceNotFoundException("Trainee not found with the id: " + id);
        }
        return traineeRepository.getTrainee(id);
    }

    @Override
    public List<Trainee> getAll() {
        return traineeRepository.getALlTrainees();
    }

    @Override
    public void create(Trainee trainee) {
        traineeRepository.addTrainee(trainee);
        LoggerUtil.getLogger().info("Trainee successfully created");
    }

    @Override
    public void update(Trainee trainee) throws InstanceNotFoundException {
        if (!traineeRepository.containsTrainee(trainee.getTraineeId())) {
            throw new InstanceNotFoundException("Trainee not found with id: " + trainee.getTraineeId());
        } else traineeRepository.updateTrainee(trainee);
        LoggerUtil.getLogger().info("Trainee successfully updated");
    }

    @Override
    public void delete(Long id) throws InstanceNotFoundException {
        if (traineeRepository.getTrainee(id) == null) {
            throw new InstanceNotFoundException("Trainee not found with the id: " + id);
        } else traineeRepository.removeTrainee(id);
        LoggerUtil.getLogger().info("The trainee was deleted successfully");
    }
}
