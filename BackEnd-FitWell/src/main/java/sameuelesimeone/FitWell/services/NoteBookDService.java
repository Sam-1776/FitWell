package sameuelesimeone.FitWell.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dao.NoteBookDDAO;
import sameuelesimeone.FitWell.dto.StatDDTO;
import sameuelesimeone.FitWell.exceptions.NotFoundException;
import sameuelesimeone.FitWell.exceptions.UnauthorizedExeption;
import sameuelesimeone.FitWell.models.NoteBook.NoteBookD;
import sameuelesimeone.FitWell.models.NoteBook.StatD;
import sameuelesimeone.FitWell.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class NoteBookDService {
    @Autowired
    NoteBookDDAO noteBookDDAO;

    @Autowired
    StatDService statDService;

    public NoteBookD create(){
        return noteBookDDAO.save(new NoteBookD());
    }

    public NoteBookD findById(UUID id){
        return noteBookDDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public NoteBookD modNote(UUID id, User user, StatDDTO statDDTO){
        NoteBookD found = this.findById(id);
        if (user.getNoteBookD().getId().equals(found.getId())){
            StatD newStat = statDService.create(statDDTO);
            List<StatD> stats = new ArrayList<>();
            if (!found.getStats().isEmpty()){
                stats.addAll(found.getStats());
            }
            stats.add(newStat);
            found.setStats(stats);
            found.setRMR(newStat.getDiet().getRMR());
            found.setWeight(statDDTO.weught());
            return noteBookDDAO.save(found);
        }
        throw new UnauthorizedExeption("you can't update this noteBook");
    }
}
