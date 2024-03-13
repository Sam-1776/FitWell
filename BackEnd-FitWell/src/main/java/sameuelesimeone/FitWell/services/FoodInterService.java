package sameuelesimeone.FitWell.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dao.FoodInterDAO;
import sameuelesimeone.FitWell.dto.FoodInterDTO;
import sameuelesimeone.FitWell.exceptions.NotFoundException;
import sameuelesimeone.FitWell.models.Diet.Category;
import sameuelesimeone.FitWell.models.Diet.Food;
import sameuelesimeone.FitWell.models.Diet.FoodsIntermediate;
import sameuelesimeone.FitWell.models.Diet.Nutrients;

import java.util.List;
import java.util.UUID;

@Service
public class FoodInterService {

    @Autowired
    FoodInterDAO foodInterDAO;

    @Autowired
    FoodService foodService;

    @Autowired
    NutrientsService nutrientsService;

    public FoodsIntermediate create(FoodInterDTO foodInterDTO){
        Food found = foodService.findById(UUID.fromString(foodInterDTO.food_id()));
        double amountCalc = foodInterDTO.amount()/100;
        int cal = (int) (found.getCalories() * amountCalc);
        List<Nutrients> nutrientsList = found.getNutrition().stream().map(el -> nutrientsService.createByFood(el, amountCalc)).toList();
        List<Category> categoryList = found.getCategories();
        return foodInterDAO.save(new FoodsIntermediate(found.getName(), foodInterDTO.amount(), nutrientsList,categoryList,cal));
    }

    public FoodsIntermediate findById(UUID id){
        return foodInterDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public FoodsIntermediate modFoods(UUID foodId,FoodInterDTO foodInterDTO){
        FoodsIntermediate found = this.findById(foodId);
        double amountCalc = foodInterDTO.amount()/100;
        found.setAmount(foodInterDTO.amount());
        found.setNutrition(found.getNutrition().stream().map(el -> nutrientsService.modNutrientsByFood(el, amountCalc)).toList());
        return foodInterDAO.save(found);
    }

    public void deleteFood(UUID id){
        FoodsIntermediate food = this.findById(id);
        foodInterDAO.delete(food);
    }


}
