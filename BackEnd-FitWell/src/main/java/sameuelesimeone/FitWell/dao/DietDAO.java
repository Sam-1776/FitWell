package sameuelesimeone.FitWell.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sameuelesimeone.FitWell.models.Diet.Diet;

import java.util.UUID;

@Repository
public interface DietDAO extends JpaRepository<Diet, UUID> {
}
