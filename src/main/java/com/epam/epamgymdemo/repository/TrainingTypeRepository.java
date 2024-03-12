package com.epam.epamgymdemo.repository;

import com.epam.epamgymdemo.model.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {
}