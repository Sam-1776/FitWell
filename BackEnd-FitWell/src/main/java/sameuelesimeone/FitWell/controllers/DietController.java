package sameuelesimeone.FitWell.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sameuelesimeone.FitWell.dto.AutoDietDTO;
import sameuelesimeone.FitWell.models.Diet.Diet;
import sameuelesimeone.FitWell.models.User;
import sameuelesimeone.FitWell.services.AutoDietService;
import sameuelesimeone.FitWell.services.DietService;

import java.util.UUID;

@RestController
@RequestMapping("/diet")
public class DietController {

    @Autowired
    AutoDietService autoDietService;

    @Autowired
    DietService dietService;

    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('USER')")
    public Diet generateAutoDiet(@RequestBody AutoDietDTO autoDiet, @AuthenticationPrincipal User currentUser){
        return autoDietService.generateDiet(autoDiet, currentUser);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('NUTRITIONIST')")
    public Diet createNewDiet(@RequestBody AutoDietDTO diet, @AuthenticationPrincipal User currentNutritionist){
        return dietService.generateNewDiet(diet, currentNutritionist);
    }

    @DeleteMapping("/{dietId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('NUTRITIONIST', 'ADMIN')")
    public void createNewDiet(@PathVariable UUID dietId, @AuthenticationPrincipal User currentNutritionist){
        dietService.deleteDiet(dietId, currentNutritionist);
    }
}
