package sameuelesimeone.FitWell.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dao.NutrientsDAO;
import sameuelesimeone.FitWell.dto.NutrientsDTO;
import sameuelesimeone.FitWell.exceptions.NotFoundException;
import sameuelesimeone.FitWell.models.Diet.Nutrients;

import java.util.UUID;

@Service
public class NutrientsService {

    @Autowired
    NutrientsDAO nutrientsDAO;


    public Nutrients create(NutrientsDTO nutrient){
        return nutrientsDAO.save(new Nutrients(nutrient.name(), nutrient.amount()));
    }

    public Nutrients createByFood(Nutrients nutrient, double amount){
        return nutrientsDAO.save(new Nutrients(nutrient.getName(), nutrient.getAmount()*amount));
    }

    public Nutrients findById(UUID id){
        return nutrientsDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Nutrients modNutrient(UUID id, NutrientsDTO nutrient){
        Nutrients found = this.findById(id);
        found.setAmount(nutrient.amount());
        nutrientsDAO.save(found);
        return found;
    }

    public Nutrients modNutrientsByFood(Nutrients nutrient, double amount){
        nutrient.setAmount(nutrient.getAmount()*amount);
        return nutrientsDAO.save(nutrient);
    }
}
