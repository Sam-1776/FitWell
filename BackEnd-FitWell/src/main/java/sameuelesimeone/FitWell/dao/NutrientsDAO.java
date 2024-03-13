package sameuelesimeone.FitWell.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sameuelesimeone.FitWell.models.Diet.Nutrients;

import java.util.UUID;

@Repository
public interface NutrientsDAO extends JpaRepository<Nutrients, UUID> {
}
