package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.model.Trainee;
import com.epam.epamgymdemo.Dao.TraineeDao;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
public class TraineeService {

    private final TraineeDao traineeDAO;

    public TraineeService(TraineeDao traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    public void createTrainee(Long traineeId, LocalDate doB, String address, Long userId) {
        traineeDAO.create(new Trainee(traineeId, doB, address, userId));
    }

    public void updateTrainee(Long traineeId, LocalDate doB, String address, Long userId) throws InstanceNotFoundException {
        traineeDAO.update(new Trainee(traineeId, doB, address, userId));
    }

    public Trainee selectTrainee(Long id) throws InstanceNotFoundException {
        return traineeDAO.get(id);
    }

    public List<Trainee> selectAllTrainees() {
        return traineeDAO.getAll();
    }

    public void deleteTrainee(Long id) throws InstanceNotFoundException {
        traineeDAO.delete(id);
    }
}
