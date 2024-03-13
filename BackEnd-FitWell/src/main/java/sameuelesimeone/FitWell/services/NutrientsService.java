package sameuelesimeone.FitWell.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dao.NutrientsDAO;
import sameuelesimeone.FitWell.dto.NutrientsDTO;
import sameuelesimeone.FitWell.exceptions.BadRequestException;
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


    public List<Nutrients> nutrientsByDiet(List<Recipe> recipes, int car, int fatty, int pro){
        Nutrients carbo = new Nutrients();
        Nutrients protein = new Nutrients();
        Nutrients fat = new Nutrients();
        double amountC = 0.0;
        double amountP = 0.0;
        double amountF = 0.0;
        for (Recipe recipe : recipes) {
            for (Nutrients nutrients1 : recipe.getNutrition()) {
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
        if (amountC == car && amountF == fatty && amountP == pro){
            Nutrients carboSave = nutrientsDAO.save(carbo);
            Nutrients proteinSave = nutrientsDAO.save(protein);
            Nutrients fatSave = nutrientsDAO.save(fat);
            List<Nutrients> nutrientsList = new ArrayList<>();
            nutrientsList.add(carboSave);
            nutrientsList.add(proteinSave);
            nutrientsList.add(fatSave);
            return nutrientsList;
        }else {
            throw new BadRequestException("Macros do not match, please try again");
        }

    }


    public List<Nutrients> modNutrientsByDiet(List<Recipe> recipes, int car, int fatty, int pro, List<Nutrients> nutrients){
        double amountC = 0.0;
        double amountP = 0.0;
        double amountF = 0.0;
        for (Recipe recipe : recipes) {
            for (Nutrients nutrients1 : recipe.getNutrition()) {
                switch (nutrients1.getName()){
                    case "carbohydrate":
                        amountC += nutrients1.getAmount();
                        break;
                    case"protein":
                        amountP += nutrients1.getAmount();
                        break;
                    default:
                        amountF += nutrients1.getAmount();
                        break;
                }
            }
        }
        if (amountC == car && amountF == fatty && amountP == pro){
            for (Nutrients nutrient : nutrients) {
                switch (nutrient.getName()){
                    case "carbohydrate":
                        nutrient.setAmount(amountC);
                        nutrientsDAO.save(nutrient);
                        break;
                    case"protein":
                        nutrient.setAmount(amountP);
                        nutrientsDAO.save(nutrient);
                        break;
                    default:
                        nutrient.setAmount(amountF);
                        nutrientsDAO.save(nutrient);
                        break;
                }
            }
            return nutrients;
        }else {
            throw new BadRequestException("Macros do not match, please try again");
        }

    }


    public List<Nutrients> modNutrientsByRecipe(List<FoodsIntermediate> foods, List<Nutrients> nutrients){
        double amountC = 0.0;
        double amountP = 0.0;
        double amountF = 0.0;
        for (FoodsIntermediate food : foods) {
            for (Nutrients nutrients1 : food.getNutrition()) {
                switch (nutrients1.getName()){
                    case "carbohydrate":
                        amountC += nutrients1.getAmount();
                        break;
                    case"protein":
                        amountP += nutrients1.getAmount();
                        break;
                    default:
                        amountF += nutrients1.getAmount();
                        break;
                }
            }
        }
        for (Nutrients nutrient : nutrients) {
            switch (nutrient.getName()){
                case "carbohydrate":
                    nutrient.setAmount(amountC);
                    nutrientsDAO.save(nutrient);
                    break;
                    case"protein":
                        nutrient.setAmount(amountP);
                        nutrientsDAO.save(nutrient);
                        break;
                    default:
                        nutrient.setAmount(amountF);
                        nutrientsDAO.save(nutrient);
                        break;
            }
        }
            return nutrients;

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
