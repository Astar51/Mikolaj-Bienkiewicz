package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
public class TrainingController {
    private final TrainingServiceImpl trainingService;

    @GetMapping
    @ResponseBody
    public List<TrainingDto> getAllTrainings() {
        return trainingService.getAllTrainings();
    }

    @GetMapping("/user/{userId}")
    @ResponseBody
    public List<TrainingDto> getAllTrainingsForUser(@PathVariable("userId") Long userId) {
        return trainingService.getAllTrainingsForUser(userId);
    }

    @PostMapping("/finished")
    @ResponseBody
    public List<TrainingDto> getAllFinishedTrainings(@RequestBody DateDto dateDto) {
        return trainingService.getAllFinishedTrainings(dateDto.date());
    }

    @GetMapping("/activity/{activityType}")
    @ResponseBody
    public List<TrainingDto> getAllTrainingsByActivityType(@PathVariable("activityType") String activityType) {
        ActivityType type = ActivityType.valueOf(activityType);
        return trainingService.getAllTrainingsByActivity(type);
    }

    @PostMapping
    public Training createTraining(@RequestBody TrainingDto trainingDto) {
        System.out.println("Training with type: " + trainingDto.activityType().toString() + "passed to the request");
        return trainingService.createTraining(trainingDto);
    }

    @PostMapping("/update")
    public void updateTraining(@RequestBody TrainingDto trainingDto){
        trainingService.updateTraining(trainingDto);
    }
}
