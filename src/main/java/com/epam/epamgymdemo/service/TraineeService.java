package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.model.*;
import com.epam.epamgymdemo.repository.TraineeRepository;
import com.epam.epamgymdemo.repository.TrainerRepository;
import com.epam.epamgymdemo.repository.TrainingRepository;
import com.epam.epamgymdemo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
@Slf4j
@AllArgsConstructor
public class TraineeService {

    private final TraineeRepository traineeRepository;

    private final TrainerRepository trainerRepository;

    private final TrainingRepository trainingRepository;

    private final UserRepository userRepository;

    @Transactional
    public Trainee create(LocalDate birthday, String address, User user) throws InstanceNotFoundException {
        Trainee trainee = Trainee.builder()
                .birthday(birthday)
                .address(address)
                .user(user)
                .trainers(new HashSet<>())
                .build();

        traineeRepository.save(trainee);

        log.info(String.format("Trainee with the id: %d successfully created", trainee.getId()));

        return trainee;
    }

    @Transactional
    public void update(Long id, LocalDate birthday, String address, Long userId) throws InstanceNotFoundException {
        Trainee trainee = this.getById(id);
        if (birthday != null) {
            trainee.setBirthday(birthday);
        }
        if (address != null) {
            trainee.setAddress(address);
        }
        if (userId != null) {
            User user = userRepository.findById(userId).orElseThrow(() -> new InstanceNotFoundException(String.format("User not found with the id: %d", userId)));
            trainee.setUser(user);
        }

        traineeRepository.save(trainee);

        log.info(String.format("Trainee with the id: %d successfully updated", id));
    }

    public Trainee getById(Long id) throws InstanceNotFoundException {
        return traineeRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException(String.format("Trainee not found with the id: %d", id)));
    }

    public Trainee getByUsername(String username) throws InstanceNotFoundException {
        User user = userRepository.findByUsername(username);

        List<Trainee> trainees = this.getAll().stream().filter(t -> t.getUser().equals(user)).toList();

        if (trainees.size() == 0) {
            throw new InstanceNotFoundException(String.format("Trainee not found with the username: %s", username));
        } else if (trainees.size() > 1) {
            throw new DuplicateKeyException(String.format("Multiple trainees found with the username : %s", username));
        } else return trainees.get(0);
    }

    public List<Trainee> getAll() {
        return traineeRepository.findAll();
    }

    public Long getUserId(Long id) throws InstanceNotFoundException {
        return this.getById(id).getUser().getId();
    }

    @Transactional
    public void deleteById(Long id) throws InstanceNotFoundException {
        Long userId = this.getById(id).getUser().getId();

        userRepository.deleteById(userId);
        traineeRepository.deleteById(id);

        log.info(String.format("Trainee with the id: %d successfully deleted", id));
    }

    @Transactional
    public void deleteByUsername(String username) throws InstanceNotFoundException {
        Long id = this.getByUsername(username).getId();

        this.deleteById(id);

        log.info(String.format("Trainee with the username: %s successfully deleted", username));
    }

    public List<Training> getTrainingsByUsernameAndCriteria(String traineeUsername, LocalDate fromDate, LocalDate toDate,
                                                            String trainerName, TrainingType trainingType) throws InstanceNotFoundException {
        Trainee trainee = this.getByUsername(traineeUsername);

        Stream<Training> trainingStream = trainingRepository.findAll().stream().filter(t -> t.getTrainee().equals(trainee));

        if (fromDate != null) {
            trainingStream = trainingStream.filter(t -> t.getTrainingDate().isEqual(fromDate) || t.getTrainingDate().isAfter(fromDate));
        }
        if (toDate != null) {
            trainingStream = trainingStream.filter(t -> t.getTrainingDate().isEqual(toDate) || t.getTrainingDate().isBefore(toDate));
        }
        if (trainerName != null) {
            trainingStream = trainingStream.filter(t -> t.getTrainer().getUser().getFirstName().equals(trainerName));
        }
        if (trainingType != null) {
            trainingStream = trainingStream.filter(t -> t.getTrainingType().getId().longValue() == trainingType.getId().longValue());
        }

        return trainingStream.toList();
    }

    public List<Trainer> getUnassignedTrainers(String username) throws InstanceNotFoundException {
        Trainee trainee = this.getByUsername(username);

        return trainerRepository.findAll().stream().filter(t -> !trainee.getTrainers().contains(t)).toList();
    }

    public Set<Trainer> getTraineeTrainersList(Long id) throws InstanceNotFoundException {
        Trainee trainee = this.getById(id);

        return trainee.getTrainers();
    }

    @Transactional
    public void addTrainerToTrainersList(Long id, Trainer trainer) throws InstanceNotFoundException {
        Trainee trainee = this.getById(id);

        trainee.getTrainers().add(trainer);
        trainer.getTrainees().add(trainee);

        log.info(String.format("Trainer with the id: %d has been added to the trainers list of trainee with the id: %d", trainer.getId(), id));
    }

    @Transactional
    public void deleteTrainerFromTrainersList(Long id, Trainer trainer) throws InstanceNotFoundException {
        Trainee trainee = this.getById(id);

        trainee.getTrainers().remove(trainer);
        trainer.getTrainees().remove(trainee);

        log.info(String.format("Trainer with the id: %d has been removed from the trainers list of trainee with the id: %d", trainer.getId(), id));
    }
}
