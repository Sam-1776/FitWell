package sameuelesimeone.FitWell.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sameuelesimeone.FitWell.models.Exercise;
import sameuelesimeone.FitWell.models.Muscle;
import sameuelesimeone.FitWell.models.Type;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExerciseDAO extends JpaRepository<Exercise, UUID> {
    Exercise findByName(String name);
    List<Exercise> findByTypeAndMuscle(Type type, Muscle muscle);

    List<Exercise> findByType(Type type);
}
