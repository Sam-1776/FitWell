package sameuelesimeone.FitWell.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sameuelesimeone.FitWell.models.CardWorkout;
import sameuelesimeone.FitWell.models.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface CardWorkoutDAO extends JpaRepository<CardWorkout, UUID> {

    List<CardWorkout> findByUser(User user);
    List<CardWorkout> findByCoach(User coach);
}
