package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.dao.TrainerDao;
import com.epam.epamgymdemo.dao.TrainingTypeDao;
import com.epam.epamgymdemo.dao.UserDao;
import com.epam.epamgymdemo.generator.UsernamePasswordGenerator;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.TrainingType;
import com.epam.epamgymdemo.model.User;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Service
public class TrainerService {

    private final TrainerDao trainerDao;
    private final UserDao userDao;
    private final TrainingTypeDao trainingTypeDao;
    private final UsernamePasswordGenerator usernamePasswordGenerator;

    public TrainerService(TrainerDao trainerDao, UserDao userDao, TrainingTypeDao trainingTypeDao) {
        this.trainerDao = trainerDao;
        this.userDao = userDao;
        this.trainingTypeDao = trainingTypeDao;
        this.usernamePasswordGenerator = new UsernamePasswordGenerator(userDao);
    }

    public void createTrainer(Long trainerId, Long typeId,
                              Long userId, String firstName, String lastName, Boolean isActive) throws InstanceNotFoundException {
        String username = usernamePasswordGenerator.generateUsername(firstName, lastName);
        String password = usernamePasswordGenerator.generatePassword();

        userDao.create(new User(userId, firstName, lastName, username, password, isActive));

        TrainingType specialization = trainingTypeDao.get(typeId);
        Trainer trainer = new Trainer(trainerId, specialization, userDao.get(userId));

        trainerDao.create(trainer);
    }

    public void updateTrainer(Long trainerId, Long typeId, Long userId) throws InstanceNotFoundException {
        TrainingType specialization = trainingTypeDao.get(typeId);
        Trainer trainer = new Trainer(trainerId, specialization, userDao.get(userId));

        trainerDao.update(trainer);
    }

    public Trainer selectTrainer(Long id) throws InstanceNotFoundException {
        return trainerDao.get(id);
    }

    public List<Trainer> selectAllTrainers() {
        return trainerDao.getAll();
    }
}
