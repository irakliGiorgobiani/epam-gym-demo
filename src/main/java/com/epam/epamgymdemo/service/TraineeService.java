package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.dao.UserDao;
import com.epam.epamgymdemo.generator.UsernamePasswordGenerator;
import com.epam.epamgymdemo.model.Trainee;
import com.epam.epamgymdemo.dao.TraineeDao;
import com.epam.epamgymdemo.model.Trainer;
import com.epam.epamgymdemo.model.User;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
public class TraineeService {

    private final TraineeDao traineeDao;
    private final UserDao userDao;
    private final UsernamePasswordGenerator usernamePasswordGenerator;

    public TraineeService(TraineeDao traineeDao, UserDao userDao) {
        this.traineeDao = traineeDao;
        this.userDao = userDao;
        this.usernamePasswordGenerator = new UsernamePasswordGenerator(userDao);
    }

    public void createTrainee(Long traineeId, LocalDate dateOfBirth, String address,
                              Long userId, String firstName, String lastName, Boolean isActive) throws InstanceNotFoundException {
        String username = usernamePasswordGenerator.generateUsername(firstName, lastName);
        String password = usernamePasswordGenerator.generatePassword();

        userDao.create(new User(userId, firstName, lastName, username, password, isActive));
        Trainee trainee = new Trainee(traineeId, dateOfBirth, address, userDao.get(userId));

        traineeDao.create(trainee);
    }

    public void updateTrainee(Long traineeId, LocalDate dateOfBirth, String address, Long userId) throws InstanceNotFoundException {
        Trainee trainee = new Trainee(traineeId, dateOfBirth, address, userDao.get(userId));

        traineeDao.update(trainee);
    }

    public Trainee selectTrainee(Long id) throws InstanceNotFoundException {
        return traineeDao.get(id);
    }

    public List<Trainee> selectAllTrainees() {
        return traineeDao.getAll();
    }

    public void deleteTrainee(Long id) throws InstanceNotFoundException {
        traineeDao.delete(id);
    }
}
