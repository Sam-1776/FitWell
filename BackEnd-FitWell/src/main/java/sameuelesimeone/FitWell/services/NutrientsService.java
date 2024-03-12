package sameuelesimeone.FitWell.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dao.NutrientsDAO;
import sameuelesimeone.FitWell.dto.NutrientsDTO;
import sameuelesimeone.FitWell.exceptions.NotFoundException;
import sameuelesimeone.FitWell.models.Diet.FoodsIntermediate;
import sameuelesimeone.FitWell.models.Diet.Nutrients;
import sameuelesimeone.FitWell.models.Diet.Recipe;

import java.util.ArrayList;
import java.util.List;
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

    public List<Nutrients> nutrientsByRecipe(List<FoodsIntermediate> foodsIntermediateList){
        Nutrients carbo = new Nutrients();
        Nutrients protein = new Nutrients();
        Nutrients fat = new Nutrients();
        double amountC = 0.0;
        double amountP = 0.0;
        double amountF = 0.0;
        for (FoodsIntermediate foodsIntermediate : foodsIntermediateList) {
            for (Nutrients nutrients1 : foodsIntermediate.getNutrition()) {
                switch (nutrients1.getName()){
                    case "carbohydrate":
                        carbo.setName(nutrients1.getName());
                        amountC += nutrients1.getAmount();
                        carbo.setAmount(amountC);
                        carbo.setUnit(nutrients1.getUnit());
                        break;
                    case"protein":
                        protein.setName(nutrients1.getName());
                        amountP += nutrients1.getAmount();
                        protein.setAmount(amountP);
                        protein.setUnit(nutrients1.getUnit());
                        break;
                    default:
                        fat.setName(nutrients1.getName());
                        amountF += nutrients1.getAmount();
                        fat.setAmount(amountF);
                        fat.setUnit(nutrients1.getUnit());
                        break;
                }
            }
        }
        Nutrients carboSave = nutrientsDAO.save(carbo);
        Nutrients proteinSave = nutrientsDAO.save(protein);
        Nutrients fatSave = nutrientsDAO.save(fat);
        List<Nutrients> nutrientsList = new ArrayList<>();
        nutrientsList.add(carboSave);
        nutrientsList.add(proteinSave);
        nutrientsList.add(fatSave);
        return nutrientsList;
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
