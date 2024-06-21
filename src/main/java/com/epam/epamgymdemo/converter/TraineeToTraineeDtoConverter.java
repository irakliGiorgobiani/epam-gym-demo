package com.epam.epamgymdemo.converter;

import com.epam.epamgymdemo.model.bo.Trainee;
import com.epam.epamgymdemo.model.dto.TraineeDto;
import com.epam.epamgymdemo.model.dto.TrainerDto;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TraineeToTraineeDtoConverter {

    public TraineeDto convert(Trainee trainee, Set<TrainerDto> trainerSet) {
        return TraineeDto.builder()
                .firstName(trainee.getUser().getFirstName())
                .lastName(trainee.getUser().getLastName())
                .username(trainee.getUser().getUsername())
                .birthday(trainee.getBirthday())
                .address(trainee.getAddress())
                .isActive(trainee.getUser().getIsActive())
                .trainers(trainerSet)
                .build();
    }
}
