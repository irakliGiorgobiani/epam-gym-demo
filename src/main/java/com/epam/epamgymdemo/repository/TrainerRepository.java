package com.epam.epamgymdemo.repository;

import com.epam.epamgymdemo.model.bo.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
}