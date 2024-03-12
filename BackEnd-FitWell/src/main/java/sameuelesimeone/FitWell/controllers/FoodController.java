package sameuelesimeone.FitWell.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sameuelesimeone.FitWell.dto.FoodDTO;
import sameuelesimeone.FitWell.dto.FoodModify;
import sameuelesimeone.FitWell.dto.NutrientsDTO;
import sameuelesimeone.FitWell.models.Diet.Food;
import sameuelesimeone.FitWell.services.FoodService;

import java.util.UUID;

@RestController
@RequestMapping("/food")
public class FoodController {

    @Autowired
    FoodService foodService;

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
}
