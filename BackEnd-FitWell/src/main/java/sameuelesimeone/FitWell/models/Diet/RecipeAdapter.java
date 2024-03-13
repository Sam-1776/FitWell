package sameuelesimeone.FitWell.models.Diet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeAdapter {
    public static Recipe convertFromAutomaticRecipe(AutomaticRecipe automaticRecipe) {
        Recipe recipe = new Recipe();
        recipe.setId(automaticRecipe.getId());
        recipe.setName(automaticRecipe.getName());
        recipe.setCalories(automaticRecipe.getCalories());

        // Convert ingredients
        List<FoodsIntermediate> foodsIntermediateList = automaticRecipe.getIngredients()
                .stream()
                .map(ingredientName -> new FoodsIntermediate(ingredientName, 0, new ArrayList<>(), new ArrayList<>(), 0))
                .collect(Collectors.toList());
        recipe.setIngredients(foodsIntermediateList);

        // Copy nutrition
        recipe.setNutrition(new ArrayList<>(automaticRecipe.getNutrition()));

        return recipe;
    }
}
