package sameuelesimeone.FitWell.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sameuelesimeone.FitWell.dto.FoodDTO;
import sameuelesimeone.FitWell.dto.FoodInterDTO;
import sameuelesimeone.FitWell.dto.FoodModify;
import sameuelesimeone.FitWell.dto.NutrientsDTO;
import sameuelesimeone.FitWell.models.Diet.Food;
import sameuelesimeone.FitWell.models.Diet.FoodsIntermediate;
import sameuelesimeone.FitWell.services.FoodInterService;
import sameuelesimeone.FitWell.services.FoodService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/food")
@CrossOrigin(origins = "http://localhost:4200")
public class FoodController {

    @Autowired
    FoodService foodService;

    @Autowired
    FoodInterService foodInterService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('NUTRITIONIST', 'ADMIN')")
    public Page<Food> takeAll(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "id") String order){
        return foodService.getFood(page, size, order);
    }

    @GetMapping("/{foodId}")
    @PreAuthorize("hasAnyAuthority('NUTRITIONIST', 'ADMIN')")
    public Food takeFood(@PathVariable UUID foodId){
        return foodService.findById(foodId);
    }


    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('NUTRITIONIST', 'ADMIN')")
    public List<Food> takeFood(@RequestParam String name){
        return foodService.findByNameContaining(name);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('NUTRITIONIST','ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Food createFood(@RequestBody FoodDTO foodDTO){
        return foodService.create(foodDTO);
    }


    @PatchMapping("/{foodId}")
    @PreAuthorize("hasAnyAuthority('NUTRITIONIST', 'ADMIN')")
    public Food modFood(@PathVariable UUID foodId, @RequestBody FoodModify foodDTO){
        return foodService.modFood(foodId, foodDTO);
    }

    @DeleteMapping("/{foodId}")
    @PreAuthorize("hasAnyAuthority('NUTRITIONIST', 'ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFood(@PathVariable UUID foodId){
        foodService.deleteFood(foodId);
    }


    @PostMapping("/foodsInter")
    @PreAuthorize("hasAnyAuthority('NUTRITIONIST','ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public FoodsIntermediate createFoodInter(@RequestBody FoodInterDTO food){
        return foodInterService.create(food);
    }

    @GetMapping("/{foodInterId}")
    @PreAuthorize("hasAnyAuthority('NUTRITIONIST', 'ADMIN')")
    public FoodsIntermediate takeFoodInter(@PathVariable UUID foodInterId){
        return foodInterService.findById(foodInterId);
    }

    @PatchMapping("/{foodInterId}")
    @PreAuthorize("hasAnyAuthority('NUTRITIONIST', 'ADMIN')")
    public FoodsIntermediate modFoodsInter(@PathVariable UUID foodInterId, @RequestBody FoodInterDTO food){
        return foodInterService.modFoods(foodInterId, food);
    }

    @DeleteMapping("/{foodInterId}")
    @PreAuthorize("hasAnyAuthority('NUTRITIONIST', 'ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFoodsInter(@PathVariable UUID foodInterId){
        foodInterService.deleteFood(foodInterId);
    }

}
