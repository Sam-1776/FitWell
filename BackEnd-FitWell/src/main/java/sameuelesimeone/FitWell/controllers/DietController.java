package sameuelesimeone.FitWell.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sameuelesimeone.FitWell.dto.AutoDietDTO;
import sameuelesimeone.FitWell.models.Diet.DietAuto;
import sameuelesimeone.FitWell.models.User;
import sameuelesimeone.FitWell.services.AutoDietService;

@RestController
@RequestMapping("/diet")
public class DietController {

    @Autowired
    AutoDietService autoDietService;

    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('USER')")
    public DietAuto generateAutoDiet(@RequestBody AutoDietDTO autoDiet, @AuthenticationPrincipal User currentUser){
        return autoDietService.generateDiet(autoDiet, currentUser);
    }
}
