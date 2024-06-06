package com.capgemini.wsb.fitnesstracker.training.api;

import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;
import com.capgemini.wsb.fitnesstracker.training.internal.TrainingDto;
import com.capgemini.wsb.fitnesstracker.user.api.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainingProvider {

    List<TrainingDto> getAllTrainings();

    List<TrainingDto> getAllTrainingsForUser(Long userId);

    List<TrainingDto> getAllFinishedTrainings(Date date);

    List<TrainingDto> getAllTrainingsByActivity(ActivityType activityType);
}
