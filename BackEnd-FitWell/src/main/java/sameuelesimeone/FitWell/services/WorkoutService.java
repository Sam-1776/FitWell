package sameuelesimeone.FitWell.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dao.SetDAO;
import sameuelesimeone.FitWell.dao.WorkoutDAO;
import sameuelesimeone.FitWell.dto.GenerateCardDTO;
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

    @Autowired
    SetDAO setDAO;

    public Workout save(WorkoutDTO workout){
        Exercise exercise = exerciseService.findById(UUID.fromString(workout.exerciseId()));
        List<Set> setList = workout.setId().stream().map(el -> setService.findById(UUID.fromString(el))).toList();
        Workout newWorkout = workoutDAO.save(new Workout(exercise, setList));
        setList.forEach(el -> {
            el.setWorkout(newWorkout);
            setDAO.save(el);
        });
        return newWorkout;
    }

    public Workout findByID(UUID id){
        return workoutDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public List<Workout> generateWorkout(GenerateCardDTO generateCardDTO){
        List<Workout> workoutList = new ArrayList<>();
        switch (generateCardDTO.partMuscle().toLowerCase()){
            case"upper": // petto spalle tricipite

                break;
            case"back": // dorso trapezio bicipite
                break;
            case"lower": // quadricipiti femorali adduttori e abduttori polpacci
                break;
            case "total": // petto dorso spalle quadricipite femorali addome
                break;
            case "warm": // per tipo cardio poly
                for (int i = 0; i < 5; i++) {
                    Exercise exercise = exerciseService.findByType(generateCardDTO.type());
                    List<Set> setList = new ArrayList<>();
                    setList.add(setService.BodyWeight(generateCardDTO.weight()));
                    Workout newWorkout = new Workout(exercise, setList);
                    workoutList.add(newWorkout);
                }
                break;
        }
        return workoutList;
    }

    public Workout generateByExp(String exp, ){
        switch (exp.toLowerCase()){
            case "low":

                break;
            case "mid":
                break;
            case "high":
                break;
        }
        return
    }
}
