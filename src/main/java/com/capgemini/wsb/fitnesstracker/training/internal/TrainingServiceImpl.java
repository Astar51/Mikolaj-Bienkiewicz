package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
class TrainingServiceImpl implements TrainingProvider, TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainingMapper trainingMapper;

    @Override
    public Training createTraining(TrainingDto trainingDto) {
        log.info("Creating Training {}", trainingDto);
        if (trainingDto.id() != null) {
            throw new IllegalArgumentException("Training has already DB ID, update is not permitted!");
        }
        var user = trainingMapper.toEntity(trainingDto);
        return trainingRepository.save(user);
    }

    @Override
    @Transactional
    public void updateTraining(TrainingDto trainingDto) {
        trainingRepository.updateTrainingDistance(trainingDto.distance(), Objects.requireNonNull(trainingDto.id()));
    }

    @Override
    public List<TrainingDto> getAllTrainings() {
        return trainingRepository.findAll()
                .stream()
                .map(trainingMapper::toAllTrainingsMapping)
                .sorted(Comparator.comparing(trainingDto -> Objects.requireNonNull(trainingDto.id())))
                .toList();
    }

    @Override
    public List<TrainingDto> getAllTrainingsForUser(Long userId) {
        if(userId == null){
            throw new IllegalArgumentException("Missing user ID!");
        }
        return trainingRepository.findAll()
                .stream()
                .filter(training -> training.getUser().getId().equals(userId))
                .map(trainingMapper::toAllTrainingsMapping)
                .sorted(Comparator.comparing(trainingDto -> Objects.requireNonNull(trainingDto.id())))
                .toList();
    }

    @Override
    public List<TrainingDto> getAllFinishedTrainings(Date date) {
        return trainingRepository.findAll()
                .stream()
                .filter(training -> training.getEndTime().before(date))
                .map(trainingMapper::toAllTrainingsMapping)
                .sorted(Comparator.comparing(trainingDto -> Objects.requireNonNull(trainingDto.id())))
                .toList();
    }

    @Override
    public List<TrainingDto> getAllTrainingsByActivity(ActivityType activityType) {
        return trainingRepository.findAll()
                .stream()
                .filter(training -> training.getActivityType().equals(activityType))
                .map(trainingMapper::toAllTrainingsMapping)
                .sorted(Comparator.comparing(trainingDto -> Objects.requireNonNull(trainingDto.id())))
                .toList();
    }
}
