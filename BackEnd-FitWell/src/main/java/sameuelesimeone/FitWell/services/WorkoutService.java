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
import sameuelesimeone.FitWell.models.Type;
import sameuelesimeone.FitWell.models.Workout;

import java.util.ArrayList;
import java.util.Iterator;
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
        switch (generateCardDTO.partMuscle()){
            case"upper": // petto spalle tricipite
                addUniqueExercise(workoutList, generateCardDTO, "chest", "upper", 3);
                addUniqueExercise(workoutList, generateCardDTO, "shoulders", "upper", 2);
                addUniqueExercise(workoutList, generateCardDTO, "triceps", "upper", 2);
                break;
            case"back": // dorso trapezio bicipite
                addUniqueExercise(workoutList, generateCardDTO, "lats", "back", 2);
                addUniqueExercise(workoutList, generateCardDTO, "back", "back", 1);
                addUniqueExercise(workoutList, generateCardDTO, "traps", "back", 1);
                addUniqueExercise(workoutList, generateCardDTO, "biceps", "back",2);
                break;
            case"lower": // quadricipiti femorali adduttori e abduttori polpacci
                Workout workoutQuadriceps = generateByExp(generateCardDTO.exp(), "quadriceps", "lower", generateCardDTO.weight());
                Workout workoutHamstrings = generateByExp(generateCardDTO.exp(), "hamstrings", "lower", generateCardDTO.weight());
                Workout workoutAdductors = generateByExp(generateCardDTO.exp(), "adductors", "lower", generateCardDTO.weight());
                Workout workoutAbductors = generateByExp(generateCardDTO.exp(), "abductors", "lower", generateCardDTO.weight());
                Workout workoutCalves = generateByExp(generateCardDTO.exp(), "calves", "lower", generateCardDTO.weight());
                workoutList.add(workoutQuadriceps);
                workoutList.add(workoutHamstrings);
                workoutList.add(workoutAdductors);
                workoutList.add(workoutAbductors);
                workoutList.add(workoutCalves);
                break;
            case "total": // petto dorso spalle quadricipite femorali addome
                addUniqueExercise(workoutList, generateCardDTO, "chest", "upper", 2);
                addUniqueExercise(workoutList, generateCardDTO, "lats", "back", 2);
                Workout workoutShoulder = generateByExp(generateCardDTO.exp(), "shoulders", "upper", generateCardDTO.weight());
                workoutList.add(workoutShoulder);
                Workout workoutQuad = generateByExp(generateCardDTO.exp(), "quadriceps", "lower", generateCardDTO.weight());
                workoutList.add(workoutQuad);
                Workout workoutHams = generateByExp(generateCardDTO.exp(), "hamstrings", "lower", generateCardDTO.weight());
                workoutList.add(workoutHams);
                Workout workoutAdd = generateByExp(generateCardDTO.exp(), "abdominals", "core", generateCardDTO.weight());
                workoutList.add(workoutAdd);
                break;
            case "warm": // per tipo cardio poly
                for (int i = 1; i < 5; i++) {
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

    public Workout generateByExp(String exp, String muscle, String partMuscle, double weight){
        Workout workout = new Workout();
        List<Set> setList = new ArrayList<>();
        Exercise exercise = exerciseService.findByTypeAndMuscle(Type.STRENGTH, muscle);
       if (partMuscle.equals("lower")){
           switch (exp.toLowerCase()){
               case "low":
                   Set setL = setService.generateLowSetLeg(weight);
                   for (int i = 0; i < 3; i++) {
                       setList.add(setL);
                   }
                   workout.setExercise(exercise);
                   workout.setSets(setList);
                   break;
               case "mid":
                   Set setM = setService.generateMidSetLeg(weight);
                   for (int i = 0; i < 4; i++) {
                       setList.add(setM);
                   }
                   workout.setExercise(exercise);
                   workout.setSets(setList);
                   break;
               case "high":
                   Set setH = setService.generateHighSetLeg(weight);
                   for (int i = 0; i < 4; i++) {
                       setList.add(setH);
                   }
                   workout.setExercise(exercise);
                   workout.setSets(setList);
                   break;
           }
           Workout newWorkout = workoutDAO.save(workout);
           setList.forEach(el -> {
               el.setWorkout(newWorkout);
               setDAO.save(el);
           });
           return workout;
       }else {
           switch (exp.toLowerCase()){
               case "low":
                   Set setL = setService.generateLowSet();
                   for (int i = 0; i < 3; i++) {
                       setList.add(setL);
                   }
                   workout.setExercise(exercise);
                   workout.setSets(setList);
                   break;
               case "mid":
                   Set setM = setService.generateMidSet();
                   for (int i = 0; i < 4; i++) {
                       setList.add(setM);
                   }
                   workout.setExercise(exercise);
                   workout.setSets(setList);
                   break;
               case "high":
                   Set setH = setService.generateHighSet();
                   for (int i = 0; i < 4; i++) {
                       setList.add(setH);
                   }
                   workout.setExercise(exercise);
                   workout.setSets(setList);
                   break;
           }
           Workout newWorkout = workoutDAO.save(workout);
           setList.forEach(el -> {
               el.setWorkout(newWorkout);
               setDAO.save(el);
           });
           return newWorkout;
       }
    }

    // Metodo di supporto per aggiungere un esercizio unico alla lista
    private void addUniqueExercise(List<Workout> workoutList, GenerateCardDTO generateCardDTO, String muscle, String category, int iter) {
        for (int i = 0; i < iter; i++) {
            Workout workout = generateByExp(generateCardDTO.exp(), muscle, category, generateCardDTO.weight());
            if (!workoutList.contains(workout)) {
                workoutList.add(workout);
            } else {
                i--; // Riprova se l'esercizio è duplicato
            }
        }
    }
}
