package sameuelesimeone.FitWell.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dao.ExerciseDAO;
import sameuelesimeone.FitWell.models.Esercizio;
import sameuelesimeone.FitWell.models.Exercise;

@Service
public class ExerciseService {

    @Autowired
    ExerciseDAO exerciseDAO;

    public Exercise save(Exercise exercise){
        return exerciseDAO.save(exercise);
    }

    public boolean presenceOfRecords(){
        if (exerciseDAO.count() > 0) return true;
        return false;
    }
}
