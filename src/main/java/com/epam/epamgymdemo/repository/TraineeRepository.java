package com.epam.epamgymdemo.repository;

import com.epam.epamgymdemo.model.bo.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {
}
