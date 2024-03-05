package sameuelesimeone.FitWell.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sameuelesimeone.FitWell.models.CardWorkout;

import java.util.UUID;

@Repository
public interface CardWorkoutDAO extends JpaRepository<CardWorkout, UUID> {
}
