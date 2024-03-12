package sameuelesimeone.FitWell.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sameuelesimeone.FitWell.dto.RecipeDTO;
import sameuelesimeone.FitWell.models.Diet.Recipe;
import sameuelesimeone.FitWell.services.RecipeService;

@RestController
@RequestMapping("/recipe")
public class RecipeController {

    @Autowired
    RecipeService recipeService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('NUTRITIONIST','ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Recipe createRecipe(@RequestBody RecipeDTO recipeDTO){
        return recipeService.createRecipe(recipeDTO);
    }
}
