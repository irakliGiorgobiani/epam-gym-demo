package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.Dao.TrainingDao;
import com.epam.epamgymdemo.model.Training;
import com.epam.epamgymdemo.model.TrainingType;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
public class TrainingService {

    private final TrainingDao trainingDAO;

    public TrainingService(TrainingDao trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    public void createTraining(Long trainingId, Long traineeId, Long trainerId, String trainingName,
                               TrainingType trainingType, LocalDate trainingDate, Integer trainingDuration) {
        trainingDAO.create(new Training(trainingId, traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration));
    }

    public Training selectTraining(Long id) throws InstanceNotFoundException {
        return trainingDAO.get(id);
    }

    public List<Training> selectAllTrainings() {
        return trainingDAO.getAll();
    }
}
