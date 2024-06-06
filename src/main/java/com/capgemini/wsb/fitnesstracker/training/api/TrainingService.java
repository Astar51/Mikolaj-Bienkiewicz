package com.capgemini.wsb.fitnesstracker.training.api;

import com.capgemini.wsb.fitnesstracker.training.internal.TrainingDto;

public interface TrainingService {
    Training createTraining(TrainingDto trainingDto);
    void updateTraining(TrainingDto trainingDto);
}
