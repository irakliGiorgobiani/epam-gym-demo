package com.epam.epamgymdemo.repository;

import com.epam.epamgymdemo.model.bo.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {

    Optional<TrainingType> findByTypeName(String trainingType);
}