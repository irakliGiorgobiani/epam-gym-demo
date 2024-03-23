package com.epam.epamgymdemo.repository;

import com.epam.epamgymdemo.model.bo.Training;
import com.epam.epamgymdemo.model.bo.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    @Query("SELECT t FROM Training t WHERE t.trainee.user.username = :traineeUsername " +
            "AND (:fromDate IS NULL OR t.trainingDate >= :fromDate) " +
            "AND (:toDate IS NULL OR t.trainingDate <= :toDate) " +
            "AND (:trainerName IS NULL OR t.trainer.user.firstName = :trainerName) " +
            "AND (:trainingType IS NULL OR t.trainingType = :trainingType)")
    List<Training> findTrainings(@Param("traineeUsername") String traineeUsername,
                                 @Param("fromDate") LocalDate fromDate,
                                 @Param("toDate") LocalDate toDate,
                                 @Param("trainerName") String trainerName,
                                 @Param("trainingType") TrainingType trainingType);

    @Query("SELECT t FROM Training t WHERE t.trainer.user.username = :trainerUsername " +
            "AND (:fromDate IS NULL OR t.trainingDate >= :fromDate) " +
            "AND (:toDate IS NULL OR t.trainingDate <= :toDate) " +
            "AND (:traineeName IS NULL OR " +
            "CONCAT(t.trainee.user.firstName, ' ', t.trainee.user.lastName) = :traineeName)")
    List<Training> findTrainings(@Param("trainerUsername") String trainerUsername,
                                 @Param("fromDate") LocalDate fromDate,
                                 @Param("toDate") LocalDate toDate,
                                 @Param("traineeName") String traineeName);
}