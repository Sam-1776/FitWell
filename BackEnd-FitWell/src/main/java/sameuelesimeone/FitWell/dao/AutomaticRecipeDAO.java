package sameuelesimeone.FitWell.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sameuelesimeone.FitWell.models.Diet.AutomaticRecipe;
import sameuelesimeone.FitWell.models.Diet.RecipeType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AutomaticRecipeDAO extends JpaRepository<AutomaticRecipe, UUID> {

    AutomaticRecipe findByName(String name);

}
