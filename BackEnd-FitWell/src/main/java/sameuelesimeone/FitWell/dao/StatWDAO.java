package sameuelesimeone.FitWell.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sameuelesimeone.FitWell.models.CardWorkout;
import sameuelesimeone.FitWell.models.NoteBook.StatW;

import java.util.List;
import java.util.UUID;

@Repository
public interface StatWDAO extends JpaRepository<StatW, UUID> {
    List<StatW> findByCardWorkout(CardWorkout cardWorkout);
}
