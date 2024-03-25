package sameuelesimeone.FitWell.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sameuelesimeone.FitWell.dto.RecipeDTO;
import sameuelesimeone.FitWell.models.Diet.Recipe;
import sameuelesimeone.FitWell.services.RecipeService;

import java.util.UUID;

@RestController
@RequestMapping("/recipe")
@CrossOrigin(origins = "http://localhost:4200")
public class RecipeController {

    @Autowired
    RecipeService recipeService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('NUTRITIONIST','ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Recipe createRecipe(@RequestBody RecipeDTO recipeDTO){
        return recipeService.createRecipe(recipeDTO);
    }

    @PatchMapping("/{recipeId}")
    @PreAuthorize("hasAnyAuthority('NUTRITIONIST','ADMIN')")
    public Recipe modRecipe(@PathVariable UUID recipeId, @RequestBody RecipeDTO recipeDTO){
        return recipeService.modRecipe(recipeId, recipeDTO);
    }

    @GetMapping("/{recipeId}")
    @PreAuthorize("hasAnyAuthority('USER','NUTRITIONIST','ADMIN')")
    public Recipe getRecipe(@PathVariable UUID recipeId){
        return recipeService.findById(recipeId);
    }

    @DeleteMapping("/{recipeId}")
    @PreAuthorize("hasAnyAuthority('NUTRITIONIST','ADMIN')")
    public void modRecipe(@PathVariable UUID recipeId){
        recipeService.deleteRecipe(recipeId);
    }


}
