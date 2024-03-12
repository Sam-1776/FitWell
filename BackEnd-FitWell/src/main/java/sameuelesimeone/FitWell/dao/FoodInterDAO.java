package sameuelesimeone.FitWell.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sameuelesimeone.FitWell.models.Diet.FoodsIntermediate;

import java.util.UUID;

@Repository
public interface FoodInterDAO extends JpaRepository<FoodsIntermediate, UUID> {
}
