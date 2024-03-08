package sameuelesimeone.FitWell.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dao.AutomaticRecipeDAO;
import sameuelesimeone.FitWell.models.Diet.AutomaticRecipe;

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
}
