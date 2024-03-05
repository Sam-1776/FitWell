package sameuelesimeone.FitWell.services;

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

import java.util.UUID;

@Service
public class ExerciseService {

    @Autowired
    ExerciseDAO exerciseDAO;

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
}
