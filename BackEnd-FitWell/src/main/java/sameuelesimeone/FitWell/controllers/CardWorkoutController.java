package sameuelesimeone.FitWell.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sameuelesimeone.FitWell.dto.CardWorkoutDTO;
import sameuelesimeone.FitWell.dto.GenerateCardDTO;
import sameuelesimeone.FitWell.models.CardWorkout;
import sameuelesimeone.FitWell.models.User;
import sameuelesimeone.FitWell.services.CardWorkoutService;

@RestController
@RequestMapping("/card_workout")
public class CardWorkoutController {

    @Autowired
    CardWorkoutService cardWorkoutService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardWorkout save(@RequestBody CardWorkoutDTO cardWorkoutDTO){
        return cardWorkoutService.save(cardWorkoutDTO);
    }

    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.CREATED)
    public CardWorkout generateCard(@RequestBody GenerateCardDTO generateCardDTO, @AuthenticationPrincipal User acctualUser){
        return cardWorkoutService.generateCard(generateCardDTO, acctualUser.getId());
    }
}
