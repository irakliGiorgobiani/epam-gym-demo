package com.epam.epamgymdemo.repository;

import com.epam.epamgymdemo.model.bo.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
}