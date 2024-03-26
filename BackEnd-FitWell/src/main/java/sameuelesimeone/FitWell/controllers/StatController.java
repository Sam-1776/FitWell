package sameuelesimeone.FitWell.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sameuelesimeone.FitWell.models.NoteBook.StatD;
import sameuelesimeone.FitWell.models.NoteBook.StatW;
import sameuelesimeone.FitWell.models.User;
import sameuelesimeone.FitWell.services.StatDService;
import sameuelesimeone.FitWell.services.StatWService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/stat")
@CrossOrigin(origins = "http://localhost:4200")
public class StatController {

    @Autowired
    StatDService statDService;

    @Autowired
    StatWService statWService;


    @GetMapping("/diet/{dietId}")
    @PreAuthorize("hasAnyAuthority('USER', 'NUTRITIONIST')")
    public List<StatD> findByDiet(@PathVariable UUID dietId, @AuthenticationPrincipal User user){
        return statDService.findBiDiet(dietId, user);
    }

    @GetMapping("/gym/{cardId}")
    @PreAuthorize("hasAnyAuthority('USER', 'COACH')")
    public List<StatW> findByCard(@PathVariable UUID cardId, @AuthenticationPrincipal User user){
        return statWService.findByWorkout(cardId, user);
    }

}
