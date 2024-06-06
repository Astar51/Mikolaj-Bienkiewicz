package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class TrainingControllerTest {
    @Mock
    private TrainingServiceImpl trainingService = new TrainingServiceImpl(null, null);
    private TrainingController trainingController;
    private final long TRAINING_ID = 101L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainingController = new TrainingController(trainingService);
    }

    @Test
    void getAllTrainings() {
        trainingController.getAllTrainings();
        verify(trainingService, times(1)).getAllTrainings();
    }

    @Test
    void getAllTrainingsForUser() {
        trainingController.getAllTrainingsForUser(101L);
        verify(trainingService, times(1)).getAllTrainingsForUser(101L);
    }

    @Test
    void getAllFinishedTrainings() {
        Date date = new Date();
        trainingController.getAllFinishedTrainings(new DateDto(date));
        verify(trainingService, times(1)).getAllFinishedTrainings(date);
    }

    @Test
    void getAllTrainingsByActivityType() {
        trainingController.getAllTrainingsByActivityType(ActivityType.RUNNING.name());
        verify(trainingService).getAllTrainingsByActivity(ActivityType.RUNNING);
    }

    @Test
    void getAllTrainingsByActivityTypeUnknownActivity() {
        assertThrows(Exception.class, () -> trainingController.getAllTrainingsByActivityType("THIS ACTIVITY DOESNT EXIST"));
    }

    @Test
    void getTraining() {
        TrainingDto trainingDto = new TrainingDto(null, null, null, null, ActivityType.RUNNING, 0, 0);
        trainingController.createTraining(trainingDto);
        verify(trainingService).createTraining(trainingDto);
    }

    @Test
    void updateTraining() {
        var trainingDto = new TrainingDto(TRAINING_ID, new User(null, null, null, null, null), null, null, ActivityType.RUNNING, 0, 0);
        trainingService.updateTraining(trainingDto);
        verify(trainingService, times(1)).updateTraining(trainingDto);
    }
}