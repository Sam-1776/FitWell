package sameuelesimeone.FitWell.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sameuelesimeone.FitWell.models.NoteBook.NoteBookW;

import java.util.UUID;

@Repository
public interface NoteBookWDAO extends JpaRepository<NoteBookW, UUID> {
}
