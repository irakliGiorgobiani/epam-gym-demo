package com.epam.epamgymdemo.converter;

import com.epam.epamgymdemo.model.bo.Trainer;
import com.epam.epamgymdemo.model.dto.TrainerDto;
import org.springframework.stereotype.Component;

@Component
public class TrainerToTrainerDtoConverter {

    public TrainerDto convert(Trainer trainer) {
        return TrainerDto.builder()
                .specializationId(trainer.getTrainingType().getId())
                .isActive(trainer.getUser().getIsActive())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .username(trainer.getUser().getUsername())
                .build();
    }
}
