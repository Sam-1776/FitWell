package sameuelesimeone.FitWell.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sameuelesimeone.FitWell.models.Diet.Recipe;

import java.util.UUID;

@Repository
public interface RecipeDAO extends JpaRepository<Recipe, UUID> {
}
