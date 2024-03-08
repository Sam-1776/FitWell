package sameuelesimeone.FitWell.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sameuelesimeone.FitWell.models.Diet.AutomaticRecipe;

import java.util.UUID;

@Repository
public interface AutomaticRecipeDAO extends JpaRepository<AutomaticRecipe, UUID> {

    AutomaticRecipe findByName(String name);
}
