package sameuelesimeone.FitWell.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dao.SetDAO;
import sameuelesimeone.FitWell.dto.SetDTO;
import sameuelesimeone.FitWell.exceptions.NotFoundException;
import sameuelesimeone.FitWell.models.Exercise;
import sameuelesimeone.FitWell.models.Set;

import java.util.UUID;


@Service
public class SetService {

    @Autowired
    SetDAO setDAO;
    public Set findById(UUID id){
        return setDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Set save(SetDTO set){
        Set newSet = new Set(set.rep(), set.weight());
        return newSet;
    }
}
