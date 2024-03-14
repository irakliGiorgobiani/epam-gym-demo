package com.epam.epamgymdemo.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Data
@Builder
public class TraineeDto extends UserDto {

    private LocalDate birthday;

    private String address;
}
