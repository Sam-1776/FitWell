package sameuelesimeone.FitWell.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dao.StatDDAO;
import sameuelesimeone.FitWell.dao.StatWDAO;
import sameuelesimeone.FitWell.dto.StatDDTO;
import sameuelesimeone.FitWell.dto.StatWDTO;
import sameuelesimeone.FitWell.exceptions.NotFoundException;
import sameuelesimeone.FitWell.exceptions.UnauthorizedExeption;
import sameuelesimeone.FitWell.models.CardWorkout;
import sameuelesimeone.FitWell.models.Diet.Diet;
import sameuelesimeone.FitWell.models.NoteBook.StatD;
import sameuelesimeone.FitWell.models.NoteBook.StatW;
import sameuelesimeone.FitWell.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class StatDService {

    @Autowired
    StatDDAO statDDAO;

    @Autowired
    DietService dietService;

    public StatD create(StatDDTO statDDTO){
        Diet found = dietService.findById(UUID.fromString(statDDTO.dietId()));
        return statDDAO.save(new StatD(found, LocalDate.now(), statDDTO.weught()));
    }

    public StatD findById(UUID id){
        return statDDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public List<StatD> findBiDiet(UUID dietId, User user){
        Diet found = dietService.findById(dietId);
        if (user.getId().equals(found.getUser().getId()) || user.getId().equals(found.getNutritionist().getId())){
            return statDDAO.findByDiet(found);
        }
        throw new UnauthorizedExeption("can't see stat of this diet");
    }
}
