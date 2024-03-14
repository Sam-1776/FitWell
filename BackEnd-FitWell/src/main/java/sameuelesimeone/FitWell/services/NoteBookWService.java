package sameuelesimeone.FitWell.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dao.NoteBookWDAO;
import sameuelesimeone.FitWell.dto.StatWDTO;
import sameuelesimeone.FitWell.exceptions.NotFoundException;
import sameuelesimeone.FitWell.exceptions.UnauthorizedExeption;
import sameuelesimeone.FitWell.models.NoteBook.NoteBookW;
import sameuelesimeone.FitWell.models.NoteBook.StatW;
import sameuelesimeone.FitWell.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class NoteBookWService {

    @Autowired
    NoteBookWDAO noteBookWDAO;

    @Autowired
    StatWService statWService;

    public NoteBookW create(){
        return noteBookWDAO.save(new NoteBookW());
    }

    public NoteBookW findById(UUID id){
        return noteBookWDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public NoteBookW modNote(UUID id, User user, StatWDTO statWDTO){
        NoteBookW found = this.findById(id);
        if (user.getNoteBookW().getId().equals(found.getId())){
            StatW newStat = statWService.create(statWDTO);
            List<StatW> stats = new ArrayList<>();
            stats.add(newStat);
            found.setStats(stats);
            return noteBookWDAO.save(found);
        }
        throw new UnauthorizedExeption("you can't update this noteBook");
    }
}
