package sameuelesimeone.FitWell.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dao.WorkoutDAO;
import sameuelesimeone.FitWell.dto.WorkoutDTO;
import sameuelesimeone.FitWell.exceptions.NotFoundException;
import sameuelesimeone.FitWell.models.Exercise;
import sameuelesimeone.FitWell.models.Set;
import sameuelesimeone.FitWell.models.Workout;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class WorkoutService {

    @Autowired
    WorkoutDAO workoutDAO;

    @Autowired
    ExerciseService exerciseService;

    @Autowired
    SetService setService;

    public Workout save(WorkoutDTO workout){
        Exercise exercise = exerciseService.findById(UUID.fromString(workout.exerciseId()));
        List<Set> setList = workout.setId().stream().map(el -> setService.findById(UUID.fromString(el))).toList();
        return workoutDAO.save(new Workout(exercise, setList));
    }

    public Workout findByID(UUID id){
        return workoutDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }
}
