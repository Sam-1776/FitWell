package sameuelesimeone.FitWell.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dao.CardWorkoutDAO;
import sameuelesimeone.FitWell.dao.UserDAO;
import sameuelesimeone.FitWell.dao.WorkoutDAO;
import sameuelesimeone.FitWell.dto.CardWorkoutDTO;
import sameuelesimeone.FitWell.dto.GenerateCardDTO;
import sameuelesimeone.FitWell.exceptions.BadRequestException;
import sameuelesimeone.FitWell.models.CardWorkout;
import sameuelesimeone.FitWell.models.Role;
import sameuelesimeone.FitWell.models.User;
import sameuelesimeone.FitWell.models.Workout;

import java.util.ArrayList;
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

    @Autowired
    UserService userService;

    @Autowired
    UserDAO userDAO;

    public CardWorkout save(CardWorkoutDTO cardWorkout){
        List<Workout> workoutList = cardWorkout.workouts_id().stream().map(el -> workoutService.findByID(UUID.fromString(el))).toList();
        User user = userService.findById(UUID.fromString(cardWorkout.user_id()));
        CardWorkout newCard = cardWorkoutDAO.save(new CardWorkout(cardWorkout.name(), workoutList, cardWorkout.restTimer(), user));
        workoutList.forEach(el -> {
            el.setCardWorkout(newCard);
            workoutDAO.save(el);
        });
        List<CardWorkout> cardWorkoutList = new ArrayList<>();
        cardWorkoutList.addAll(user.getWorkouts());
        cardWorkoutList.add(newCard);
        user.setWorkouts(cardWorkoutList);
        userDAO.save(user);

        if (cardWorkout.coach_id() != null){
            User coach = userService.findById(UUID.fromString(cardWorkout.coach_id()));
            coach.getRole().forEach(el -> {
                if (el.equals(Role.COACH)){
                    newCard.setCoach(coach);
                    cardWorkoutDAO.save(newCard);
                }else {
                    throw new BadRequestException("The entered user with ID:" + coach.getId() + " is not a coach.");
                }
            });
        }

        return newCard;
    }

    public CardWorkout generateCard(GenerateCardDTO generateCardDTO){

    }



}
