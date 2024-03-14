package sameuelesimeone.FitWell.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dao.StatWDAO;
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
public class StatWService {

    @Autowired
    StatWDAO statWDAO;

    @Autowired
    CardWorkoutService cardWorkoutService;

    public StatW create(StatWDTO statWDTO){
        CardWorkout found = cardWorkoutService.findById(UUID.fromString(statWDTO.cardWorkoutId()));
        return statWDAO.save(new StatW(found, LocalDate.now()));
    }

    public StatW findById(UUID id){
        return statWDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }


    public List<StatW> findByWorkout(UUID cardWorkoutId, User user){
        CardWorkout found = cardWorkoutService.findById(cardWorkoutId);
        if (user.getId().equals(found.getUser().getId()) || user.getId().equals(found.getCoach().getId())){
            return statWDAO.findByCardWorkout(found);
        }
        throw new UnauthorizedExeption("can't see stat of this cardWorkout");
    }
}
