package sameuelesimeone.FitWell.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dao.CardWorkoutDAO;
import sameuelesimeone.FitWell.dao.WorkoutDAO;
import sameuelesimeone.FitWell.dto.CardWorkoutDTO;
import sameuelesimeone.FitWell.models.CardWorkout;
import sameuelesimeone.FitWell.models.Workout;

import java.util.List;
import java.util.UUID;

@Service
public class CardWorkoutService {

    @Autowired
    CardWorkoutDAO cardWorkoutDAO;

    @Autowired
    WorkoutService workoutService;


    @Autowired
    WorkoutDAO workoutDAO;

    public CardWorkout save(CardWorkoutDTO cardWorkout){
        List<Workout> workoutList = cardWorkout.workouts_id().stream().map(el -> workoutService.findByID(UUID.fromString(el))).toList();
        CardWorkout newCard = cardWorkoutDAO.save(new CardWorkout(cardWorkout.name(), workoutList, cardWorkout.restTimer()));
        workoutList.forEach(el -> {
            el.setCardWorkout(newCard);
            workoutDAO.save(el);
        });
        return newCard;
    }

}
