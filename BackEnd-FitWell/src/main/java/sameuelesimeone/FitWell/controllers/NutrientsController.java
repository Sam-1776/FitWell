package sameuelesimeone.FitWell.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sameuelesimeone.FitWell.dto.NutrientsDTO;
import sameuelesimeone.FitWell.models.Diet.Nutrients;
import sameuelesimeone.FitWell.services.NutrientsService;

import java.util.UUID;

@RestController
@RequestMapping("/nutrients")
@CrossOrigin(origins = "http://localhost:4200")
public class NutrientsController {
    @Autowired
    NutrientsService nutrientsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('NUTRITIONIST')")
    public Nutrients create(@RequestBody NutrientsDTO nutrients){
        return nutrientsService.create(nutrients);
    }

    @PatchMapping("/{nutrientId}")
    @PreAuthorize("hasAnyAuthority('NUTRITIONIST')")
    public Nutrients modNutrient(@PathVariable UUID nutrientId, @RequestBody NutrientsDTO nutrients){
        return  nutrientsService.modNutrient(nutrientId, nutrients);
    }
}
