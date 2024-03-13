package sameuelesimeone.FitWell.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dao.RecipeDAO;
import sameuelesimeone.FitWell.dto.RecipeDTO;
import sameuelesimeone.FitWell.exceptions.NotFoundException;
import sameuelesimeone.FitWell.models.Diet.FoodsIntermediate;
import sameuelesimeone.FitWell.models.Diet.Nutrients;
import sameuelesimeone.FitWell.models.Diet.Recipe;

import java.util.List;
import java.util.UUID;

@Service
public class RecipeService {

    @Autowired
    RecipeDAO recipeDAO;

    @Autowired
    FoodInterService foodInterService;

    @Autowired
    NutrientsService nutrientsService;

    public Recipe createRecipe(RecipeDTO recipeDTO){
        List<FoodsIntermediate> ingrediets = recipeDTO.food_id().stream()
                .map(el -> foodInterService.findById(UUID.fromString(el))).toList();
        int calories = ingrediets.stream().mapToInt(FoodsIntermediate::getCalories).sum();
        List<Nutrients> nutrients = nutrientsService.nutrientsByRecipe(ingrediets);
        return recipeDAO.save(new Recipe(recipeDTO.name(), ingrediets, calories, nutrients));
    }

    public Recipe findById(UUID id){
        return recipeDAO.findById(id).orElseThrow(()-> new NotFoundException(id));
    }

    public void deleteRecipe(UUID id){
        Recipe found = this.findById(id);
        recipeDAO.delete(found);
    }
}
