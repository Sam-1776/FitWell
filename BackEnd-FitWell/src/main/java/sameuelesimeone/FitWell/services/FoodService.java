package sameuelesimeone.FitWell.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.dao.FoodDAO;
import sameuelesimeone.FitWell.dao.FoodInterDAO;
import sameuelesimeone.FitWell.dao.NutrientsDAO;
import sameuelesimeone.FitWell.dto.FoodDTO;
import sameuelesimeone.FitWell.dto.FoodModify;
import sameuelesimeone.FitWell.dto.NutrientsDTO;
import sameuelesimeone.FitWell.exceptions.BadRequestException;
import sameuelesimeone.FitWell.exceptions.NotFoundException;
import sameuelesimeone.FitWell.models.Diet.Category;
import sameuelesimeone.FitWell.models.Diet.Food;
import sameuelesimeone.FitWell.models.Diet.Nutrients;
import sameuelesimeone.FitWell.models.User;

import java.util.List;
import java.util.UUID;

@Service
public class FoodService {
    @Autowired
    FoodDAO foodDAO;

    @Autowired
    NutrientsService nutrientsService;

    @Autowired
    NutrientsDAO nutrientsDAO;

    public Page<Food> getFood(int pageN, int pageS, String OrderBy) {
        if (pageS > 20) pageS = 20;
        Pageable pageable = PageRequest.of(pageN, pageS, Sort.by(OrderBy));
        return foodDAO.findAll(pageable);
    }

    public Food create(FoodDTO food){
        foodDAO.findByName(food.name()).ifPresent(el -> {
            throw new BadRequestException("The food is already registered");
        });
        Food newFood = new Food();
        List<Nutrients> nutrients = food.nutrients_id().stream().map(el -> nutrientsService.findById(UUID.fromString(el))).toList();
        List<Category> categories = setCategory(food.category());
        newFood.setName(food.name());
        newFood.setAmount(100.00);
        newFood.setUnit("g");
        newFood.setNutrition(nutrients);
        newFood.setCategories(categories);
        newFood.setCalories(food.calories());

        Food foodSaved = foodDAO.save(newFood);
        return foodSaved;
    }

    public Food findById(UUID id){
        return foodDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Food modFood(UUID id, FoodModify foodModify){
        Food found = this.findById(id);
        found.setName(foodModify.foodDTO().name());
        if(foodModify.nutrientsDTO() != null){
            found.setNutrition(foodModify.foodDTO().nutrients_id().stream().map(el -> nutrientsService.modNutrient(UUID.fromString(el), foodModify.nutrientsDTO())).toList());
        }
        found.setCategories(setCategory(foodModify.foodDTO().category()));
        return foodDAO.save(found);
    }

    public void deleteFood(UUID id){
        Food found = this.findById(id);
        foodDAO.delete(found);
    }


    public List<Category> setCategory(List<String> category){
        List<Category> foodCategory = category.stream().map(el -> {
            switch (el.toLowerCase()) {
                case "cereals and tubers":
                    return Category.CEREALS_AND_TUBERS;
                case "fruits and vegetables":
                    return Category.FRUITS_AND_VEGETABLES;
                case "milk and dairy products":
                    return Category.MILK_AND_DAIRY_PRODUCTS;
                case "meat":
                    return Category.MEAT;
                case "fish":
                    return Category.FISH;
                case "eggs":
                    return Category.EGGS;
                case "seasoning fats":
                    return Category.SEASONING_FATS;
                default:
                    throw new IllegalArgumentException("Unknown category: " + el);
            }
        }).toList();

        return foodCategory;
    }

    public List<Food> findByNameContaining(String name){
        return foodDAO.findByNameContaining(name);
    }

}
