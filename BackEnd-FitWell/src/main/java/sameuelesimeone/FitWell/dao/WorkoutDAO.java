package sameuelesimeone.FitWell.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sameuelesimeone.FitWell.models.Workout;

import java.util.UUID;

@Repository
public interface WorkoutDAO extends JpaRepository<Workout, UUID> {
}
