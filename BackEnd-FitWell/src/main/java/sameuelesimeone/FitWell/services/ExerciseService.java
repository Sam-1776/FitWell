package sameuelesimeone.FitWell.services;

import ch.qos.logback.core.encoder.EchoEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dao.ExerciseDAO;
import sameuelesimeone.FitWell.exceptions.NotFoundException;
import sameuelesimeone.FitWell.models.Esercizio;
import sameuelesimeone.FitWell.models.Exercise;
import sameuelesimeone.FitWell.models.Muscle;
import sameuelesimeone.FitWell.models.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class ExerciseService {

    @Autowired
    ExerciseDAO exerciseDAO;

    private Random rdm = new Random();

    public Page<Exercise> getExercises(int pageN, int size, String orderBy){
        Pageable pageable = PageRequest.of(pageN, size, Sort.by(orderBy));
        return exerciseDAO.findAll(pageable);
    }

    public Exercise findById(UUID id){
        return exerciseDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Exercise save(Exercise exercise){
        return exerciseDAO.save(exercise);
    }

    public boolean presenceOfRecords(){
        if (exerciseDAO.count() > 0) return true;
        return false;
    }

    public Exercise findByName(String name){
        return exerciseDAO.findByName(name);
    }

    public Exercise findByTypeAndMuscle(Type type, String muscle){
        List<Exercise> exerciseList = new ArrayList<>();
        switch (muscle.toLowerCase()){
            case "chest":
                exerciseList.addAll(exerciseDAO.findByTypeAndMuscle(Type.STRENGTH, Muscle.CHEST));
                break;
            case "shoulders":
                exerciseList.addAll(exerciseDAO.findByTypeAndMuscle(Type.STRENGTH, Muscle.SHOULDERS));
                break;
            case "triceps":
                exerciseList.addAll(exerciseDAO.findByTypeAndMuscle(Type.STRENGTH, Muscle.TRICEPS));
                break;
            case "lats":
                exerciseList.addAll(exerciseDAO.findByTypeAndMuscle(Type.STRENGTH, Muscle.LATS));
                break;
            case "back":
                exerciseList.addAll(exerciseDAO.findByTypeAndMuscle(Type.STRENGTH, Muscle.MIDDLE_BACK));
                break;
            case "traps":
                exerciseList.addAll(exerciseDAO.findByTypeAndMuscle(Type.STRENGTH, Muscle.TRAPS));
                break;
            case "biceps":
                exerciseList.addAll(exerciseDAO.findByTypeAndMuscle(Type.STRENGTH, Muscle.BICEPS));
                break;
            case "quadriceps":
                exerciseList.addAll(exerciseDAO.findByTypeAndMuscle(Type.STRENGTH, Muscle.QUADRICEPS));
                break;
            case "hamstrings":
                exerciseList.addAll(exerciseDAO.findByTypeAndMuscle(Type.STRENGTH, Muscle.HAMSTRINGS));
                break;
            case "adductors":
                exerciseList.addAll(exerciseDAO.findByTypeAndMuscle(Type.STRENGTH, Muscle.ADDUCTORS));
                break;
            case "abductors":
                exerciseList.addAll(exerciseDAO.findByTypeAndMuscle(Type.STRENGTH, Muscle.ABDUCTORS));
                break;
            case "calves":
                exerciseList.addAll(exerciseDAO.findByTypeAndMuscle(Type.STRENGTH, Muscle.CALVES));
                break;
            case "abdominals":
                exerciseList.addAll(exerciseDAO.findByTypeAndMuscle(Type.STRENGTH, Muscle.ABDOMINALS));
                break;
        }
        int count = rdm.nextInt(exerciseList.size());
        return exerciseList.get(count);
    }

    public Exercise findByType(String type){
        List<Exercise> exerciseList = new ArrayList<>();
        if (type.toLowerCase().equals(Type.CARDIO.toString().toLowerCase())){
            exerciseList.addAll(exerciseDAO.findByType(Type.CARDIO));
        }else {
            exerciseList.addAll(exerciseDAO.findByType(Type.PLYOMETRICS));
        }
        int count = rdm.nextInt(exerciseList.size());
        return exerciseList.get(count);
    }
}
