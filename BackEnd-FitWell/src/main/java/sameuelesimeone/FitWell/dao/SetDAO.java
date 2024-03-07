package sameuelesimeone.FitWell.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sameuelesimeone.FitWell.models.Set;

import java.util.UUID;

@Repository
public interface SetDAO extends JpaRepository<Set, UUID> {
}
