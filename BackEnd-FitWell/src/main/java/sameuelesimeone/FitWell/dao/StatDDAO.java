package sameuelesimeone.FitWell.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sameuelesimeone.FitWell.models.Diet.Diet;
import sameuelesimeone.FitWell.models.NoteBook.StatD;

import java.util.List;
import java.util.UUID;

@Repository
public interface StatDDAO extends JpaRepository<StatD, UUID> {
    List<StatD> findByDiet(Diet diet);
}
