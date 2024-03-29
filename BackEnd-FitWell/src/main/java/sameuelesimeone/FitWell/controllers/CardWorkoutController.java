package sameuelesimeone.FitWell.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sameuelesimeone.FitWell.dto.CardWorkoutDTO;
import sameuelesimeone.FitWell.dto.GenerateCardDTO;
import sameuelesimeone.FitWell.dto.MailRequestCoachDTO;
import sameuelesimeone.FitWell.models.CardWorkout;
import sameuelesimeone.FitWell.models.User;
import sameuelesimeone.FitWell.services.CardWorkoutService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/card_workout")
@CrossOrigin(origins = "http://localhost:4200")
public class CardWorkoutController {

    @Autowired
    CardWorkoutService cardWorkoutService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'COACH')")
    public List<CardWorkout> getMyCardWorkout(@AuthenticationPrincipal User current){
        return cardWorkoutService.getAllCard(current);
    }

    @GetMapping("/coach")
    @PreAuthorize("hasAnyAuthority('COACH')")
    public List<CardWorkout> getAllCardCoach(@AuthenticationPrincipal User current){
        return cardWorkoutService.getAllCardCoach(current);
    }

    @GetMapping("/{cardID}")
    @PreAuthorize("hasAnyAuthority('USER', 'COACH', 'ADMIN')")
    public CardWorkout findById(@PathVariable UUID cardID){
        return cardWorkoutService.findById(cardID);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('USER', 'COACH')")
    public CardWorkout save(@RequestBody CardWorkoutDTO cardWorkoutDTO, @AuthenticationPrincipal User currentUser){
        return cardWorkoutService.save(cardWorkoutDTO, currentUser);
    }

    @PostMapping("/coach")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('COACH')")
    public CardWorkout saveCardCoach(@RequestBody CardWorkoutDTO cardWorkoutDTO, @AuthenticationPrincipal User currentUser){
        return cardWorkoutService.saveNewCardCoach(cardWorkoutDTO, currentUser);
    }

    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('USER')")
    public CardWorkout generateCard(@RequestBody GenerateCardDTO generateCardDTO, @AuthenticationPrincipal User acctualUser){
        return cardWorkoutService.generateCard(generateCardDTO, acctualUser.getId());
    }

    @PatchMapping("/{cardId}")
    @PreAuthorize("hasAnyAuthority('USER', 'COACH')")
    public CardWorkout modCard(@PathVariable UUID cardId, @AuthenticationPrincipal User currentUser, @RequestBody CardWorkoutDTO cardWorkout){
        return cardWorkoutService.modCardWorkout(currentUser.getId(),cardWorkout, cardId);
    }

    @DeleteMapping("/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('USER', 'COACH', 'ADMIN')")
    public void deleteCard(@PathVariable UUID cardId, @AuthenticationPrincipal User currentUser){
        cardWorkoutService.deleteCard(cardId, currentUser);
    }


    @PostMapping("/requestMail")
    @PreAuthorize("hasAnyAuthority('USER')")
    public void RequestOnCardWorkout(@AuthenticationPrincipal User user, @RequestBody MailRequestCoachDTO mail){
        cardWorkoutService.requestOnCard(user, mail);
    }

}
