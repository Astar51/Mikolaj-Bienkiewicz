package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static java.time.ZoneOffset.UTC;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

class TrainingServiceImplTest {

    @Mock
    private TrainingRepository trainingRepository;
    private TrainingServiceImpl trainingService;
    private Training training1;
    private Training training2;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainingService = new TrainingServiceImpl(trainingRepository, new TrainingMapper());

        user1 = new User(1L,
                "firstName1",
                "lastName1",
                LocalDate.of(2000, 1, 15),
                "email");
        user2 = new User(2L,
                "firstName2",
                "lastName2",
                LocalDate.of(1980, 1, 15),
                "email2");
        training1 = new Training(
                1L,
                user1,
                Date.from(LocalDateTime.of(2024, 1, 15, 20, 0, 0).toInstant(UTC)),
                Date.from(LocalDateTime.of(2024, 1, 15, 21, 0, 0).toInstant(UTC)),
                ActivityType.RUNNING,
                10.0,
                6.0);

        training2 = new Training(
                2L,
                user2,
                Date.from(LocalDateTime.of(2024, 6, 15, 15, 0, 0).toInstant(UTC)),
                Date.from(LocalDateTime.of(2024, 6, 15, 17, 0, 0).toInstant(UTC)),
                ActivityType.CYCLING,
                30.0,
                15.0);

        var training3 = new Training(
                3L,
                user2,
                Date.from(LocalDateTime.of(2024, 6, 16, 20, 0, 0).toInstant(UTC)),
                Date.from(LocalDateTime.of(2024, 6, 16, 21, 0, 0).toInstant(UTC)),
                ActivityType.CYCLING,
                40.0,
                20.0);

        when(trainingRepository.findAll()).thenReturn(
                List.of(
                        training1,
                        training2,
                        training3));
    }

    @Test
    void createTraining() {
        Date startTime = Date.from(LocalDateTime.of(2024, 6, 16, 20, 0, 0).toInstant(UTC));
        Date endTime = Date.from(LocalDateTime.of(2024, 6, 16, 21, 0, 0).toInstant(UTC));
        var training = new TrainingDto(
                null,
                user2,
                startTime,
                endTime,
                ActivityType.CYCLING,
                40.0,
                20.0);
        trainingService.createTraining(training);
        verify(trainingRepository).save(new Training(user2, startTime, endTime, ActivityType.CYCLING, 40.0, 20.0));
    }

    @Test
    void updateTraining() {
        Date startTime = Date.from(LocalDateTime.of(2024, 6, 16, 20, 0, 0).toInstant(UTC));
        Date endTime = Date.from(LocalDateTime.of(2024, 6, 16, 21, 0, 0).toInstant(UTC));
        trainingService.updateTraining(new TrainingDto(
                101L,
                user2,
                startTime,
                endTime,
                ActivityType.CYCLING,
                40.0,
                20.0));
        verify(trainingRepository).updateTrainingDistance(40.0, 101L);
    }

    @Test
    void getAllTrainings() {
        List<TrainingDto> allTrainings = trainingService.getAllTrainings();
        assertEquals(3, allTrainings.size());
    }

    @Test
    void getAllTrainingsForUser() {
        List<TrainingDto> allTrainingsForUser = trainingService.getAllTrainingsForUser(2L);
        assertEquals(2, allTrainingsForUser.size());
    }

    @Test
    void getAllFinishedTrainings() {
        Date allFinishedTrainingsDate = Date.from(LocalDateTime.of(2024, 6, 16, 22, 0, 0).toInstant(UTC));
        List<TrainingDto> allFinishedTrainings = trainingService.getAllFinishedTrainings(allFinishedTrainingsDate);
        assertEquals(3, allFinishedTrainings.size());


        Date oneFinishedTrainingDate = Date.from(LocalDateTime.of(2024, 1, 15, 21, 1, 0).toInstant(UTC));
        List<TrainingDto> oneFinishedTraining = trainingService.getAllFinishedTrainings(oneFinishedTrainingDate);
        assertEquals(1, oneFinishedTraining.size());
    }

    @Test
    void getAllTrainingsByActivity() {
        List<TrainingDto> allTrainingsByActivity = trainingService.getAllTrainingsByActivity(ActivityType.CYCLING);
        assertEquals(2, allTrainingsByActivity.size());
        allTrainingsByActivity.forEach(activity -> assertEquals(ActivityType.CYCLING, activity.activityType()));
    }
}