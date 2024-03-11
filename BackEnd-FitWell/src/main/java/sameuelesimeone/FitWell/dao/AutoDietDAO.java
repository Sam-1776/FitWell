package sameuelesimeone.FitWell.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sameuelesimeone.FitWell.models.Diet.DietAuto;

import java.util.UUID;

@Repository
public interface AutoDietDAO extends JpaRepository<DietAuto, UUID> {
}
