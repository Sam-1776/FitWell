package sameuelesimeone.FitWell.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sameuelesimeone.FitWell.models.Diet.Food;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FoodDAO extends JpaRepository<Food, UUID> {
    Optional<Food> findByName(String name);
}
