package sameuelesimeone.FitWell.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dao.AutomaticRecipeDAO;
import sameuelesimeone.FitWell.exceptions.NotFoundException;
import sameuelesimeone.FitWell.models.Diet.AutomaticRecipe;
import sameuelesimeone.FitWell.models.Diet.RecipeType;

import java.util.ArrayList;
import java.util.List;

@Service
public class AutomaticRecipeService {

    @Autowired
    AutomaticRecipeDAO automaticRecipeDAO;

    public AutomaticRecipe findByName(String name){
        return automaticRecipeDAO.findByName(name);
    }

    public boolean presenceOfRecords(){
        if (automaticRecipeDAO.count() > 0) return true;
        return false;
    }

    public List<AutomaticRecipe> findByType(RecipeType type){
        List<AutomaticRecipe> recipeList = new ArrayList<>();
        List<AutomaticRecipe> recipes = automaticRecipeDAO.findAll();
        for (AutomaticRecipe recipe : recipes) {
            for (RecipeType recipeType : recipe.getRecipeTypes()) {
                if (recipeType.equals(type)){
                    recipeList.add(recipe);
                }
            }
        }
        return recipeList;
    }


}
