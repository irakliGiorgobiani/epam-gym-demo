package com.epam.epamgymdemo.converter;

import com.epam.epamgymdemo.model.bo.Trainer;
import com.epam.epamgymdemo.model.dto.TrainerWithListDto;
import com.epam.epamgymdemo.model.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TrainerToTrainerWithListDtoConverter {

    public TrainerWithListDto convert(Trainer trainer, Set<UserDto> traineeSet) {
        return TrainerWithListDto.builder()
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .isActive(trainer.getUser().getIsActive())
                .specializationId(trainer.getTrainingType().getId())
                .username(trainer.getUser().getUsername())
                .trainees(traineeSet)
                .build();
    }
}
