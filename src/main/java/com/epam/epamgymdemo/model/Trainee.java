package com.epam.epamgymdemo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
@Getter
@AllArgsConstructor
public class Trainee {
    private Long traineeId;
    private LocalDate doB;
    private String address;
    private Long userId;
}
