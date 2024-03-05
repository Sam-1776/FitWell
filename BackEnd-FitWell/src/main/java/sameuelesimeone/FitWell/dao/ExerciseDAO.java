package sameuelesimeone.FitWell.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sameuelesimeone.FitWell.models.Exercise;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExerciseDAO extends JpaRepository<Exercise, UUID> {
    Exercise findByName(String name);
}
