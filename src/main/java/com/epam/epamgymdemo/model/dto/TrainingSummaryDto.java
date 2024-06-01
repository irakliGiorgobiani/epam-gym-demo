package com.epam.epamgymdemo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingSummaryDto {

    private String username;

    private String firstName;

    private String lastName;

    private String status;

    private Map<Integer, Map<Integer, Double>> yearlySummary;
}

