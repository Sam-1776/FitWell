package sameuelesimeone.FitWell.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dao.SetDAO;
import sameuelesimeone.FitWell.dto.SetDTO;
import sameuelesimeone.FitWell.exceptions.NotFoundException;
import sameuelesimeone.FitWell.models.Exercise;
import sameuelesimeone.FitWell.models.Set;

import java.util.Random;
import java.util.UUID;


@Service
public class SetService {

    @Autowired
    SetDAO setDAO;

    private Random random = new Random();
    public Set findById(UUID id){
        return setDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Set save(SetDTO set){
        Set newSet = setDAO.save(new Set(set.rep(), set.weight()));
        return newSet;
    }

    public Set generateLowSet(){
        return setDAO.save(new Set(3, random.nextDouble(2.5, 8.0)));
    }

    public Set generateMidSet(){
        return setDAO.save(new Set(4, random.nextDouble(10.0,30.0)));
    }

    public Set generateHighSet(){
        return setDAO.save(new Set(4, random.nextDouble(32.0,55.0)));
    }

    public Set generateLowSetLeg(double weight){
        return setDAO.save(new Set(3, weight+random.nextDouble(2.5, 8.0)));
    }

    public Set generateMidSetLeg(double weight){
        return setDAO.save(new Set(4, weight+random.nextDouble(10.0,30.0)));
    }

    public Set generateHighSetLeg(double weight){
        return setDAO.save(new Set(4, weight+random.nextDouble(32.0,55.0)));
    }

    public Set BodyWeight(double weight){
        return setDAO.save(new Set(1, weight));
    }

}
