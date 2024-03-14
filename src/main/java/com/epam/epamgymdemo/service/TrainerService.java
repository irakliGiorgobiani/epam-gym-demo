package com.epam.epamgymdemo.service;

import com.epam.epamgymdemo.model.bo.User;
import com.epam.epamgymdemo.model.bo.TrainingType;
import com.epam.epamgymdemo.model.bo.Trainer;
import com.epam.epamgymdemo.model.bo.Training;
import com.epam.epamgymdemo.repository.TrainerRepository;
import com.epam.epamgymdemo.repository.TrainingRepository;
import com.epam.epamgymdemo.repository.TrainingTypeRepository;
import com.epam.epamgymdemo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@AllArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepository;

    private final TrainingRepository trainingRepository;

    private final TrainingTypeRepository trainingTypeRepository;

    private final UserRepository userRepository;

    @Transactional
    public Trainer create(Long typeId, User user) throws InstanceNotFoundException {
        TrainingType specialization = trainingTypeRepository.findById(typeId)
                .orElseThrow(() -> new InstanceNotFoundException(String.format("Training type not found with the id: %d", typeId)));

        Trainer trainer = Trainer.builder()
                .trainingType(specialization)
                .user(user)
                .build();

        trainerRepository.save(trainer);

        log.info(String.format("Trainer with the id: %d successfully created", trainer.getId()));

        return trainer;
    }

    @Transactional
    public void update(Long id, Long typeId, Long userId) throws InstanceNotFoundException {
        Trainer trainer = this.getById(id);

        if (typeId != null) {
            trainer.setTrainingType(trainingTypeRepository.findById(typeId)
                    .orElseThrow(() -> new InstanceNotFoundException(String.format("Training type not found with the id: %d", typeId))));
        }
        if (userId != null) {
            trainer.setUser(userRepository.findById(userId)
                    .orElseThrow(() -> new InstanceNotFoundException(String.format("User not found with the id: %d", userId))));
        }

        trainerRepository.save(trainer);

        log.info(String.format("Trainer with the id: %d successfully updated", id));
    }

    public Trainer getById(Long id) throws InstanceNotFoundException {
        return trainerRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException(String.format("Trainer not found with the id: %d", id)));
    }

    public Trainer getByUsername(String username) throws InstanceNotFoundException {
        User user = userRepository.findByUsername(username);

        List<Trainer> trainers = trainerRepository.findAll().stream().filter(t -> t.getUser().equals(user)).toList();

        if (trainers.size() == 0) {
            throw new InstanceNotFoundException(String.format("Trainer with not found with the username: %s", username));
        } else if (trainers.size() > 1) {
            throw new DuplicateKeyException(String.format("Multiple trainers found with the username : %s", username));
        } else return trainers.get(0);
    }

    public List<Trainer> getByUsernames(List<String> usernames) throws InstanceNotFoundException {
        List<Trainer> trainers = new ArrayList<>();

        for (String username : usernames) {
            trainers.add(this.getByUsername(username));
        }

        return trainers;
    }

    public Long getUserId(Long id) throws InstanceNotFoundException {
        return this.getById(id).getUser().getId();
    }

    public List<Trainer> getAll() {
        return trainerRepository.findAll();
    }

    public List<Training> getTrainingsByUsernameAndCriteria(String trainerUsername, LocalDate fromDate, LocalDate toDate,
                                                            String traineeName) throws InstanceNotFoundException {
        Trainer trainer = this.getByUsername(trainerUsername);

        Stream<Training> trainingStream = trainingRepository.findAll().stream().filter(t -> t.getTrainer().equals(trainer));

        if (fromDate != null) {
            trainingStream = trainingStream.filter(t -> t.getTrainingDate().isEqual(fromDate) || t.getTrainingDate().isAfter(fromDate));
        }
        if (toDate != null) {
            trainingStream = trainingStream.filter(t -> t.getTrainingDate().isEqual(toDate) || t.getTrainingDate().isBefore(toDate));
        }
        if (traineeName != null) {
            trainingStream = trainingStream.filter(t -> t.getTrainee().getUser().getFirstName().equals(traineeName));
        }

        return trainingStream.toList();
    }
}
