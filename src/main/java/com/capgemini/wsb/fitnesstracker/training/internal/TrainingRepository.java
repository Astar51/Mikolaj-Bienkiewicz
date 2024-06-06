package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface TrainingRepository extends JpaRepository<Training, Long> {

    @Modifying
    @Query("update Training t set t.distance = :distance where t.id = :trainingId")
    void updateTrainingDistance(@Param("distance") double distance, @Param("trainingId") long trainingId);
}
