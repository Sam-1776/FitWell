package sameuelesimeone.FitWell.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sameuelesimeone.FitWell.dto.CardWorkoutDTO;
import sameuelesimeone.FitWell.models.CardWorkout;
import sameuelesimeone.FitWell.services.CardWorkoutService;

@RestController
@RequestMapping("/card_workout")
public class CardWorkoutController {

    @Autowired
    CardWorkoutService cardWorkoutService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardWorkout save(CardWorkoutDTO cardWorkoutDTO){
        return cardWorkoutService.save(cardWorkoutDTO);
    }
}
