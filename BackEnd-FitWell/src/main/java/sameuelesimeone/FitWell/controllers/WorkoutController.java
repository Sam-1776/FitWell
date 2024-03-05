package sameuelesimeone.FitWell.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sameuelesimeone.FitWell.dto.SetDTO;
import sameuelesimeone.FitWell.dto.WorkoutDTO;
import sameuelesimeone.FitWell.models.Set;
import sameuelesimeone.FitWell.models.Workout;
import sameuelesimeone.FitWell.services.WorkoutService;

import java.util.UUID;

@RestController
@RequestMapping("/workout")
public class WorkoutController {

    @Autowired
    WorkoutService workoutService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Workout create(@RequestBody WorkoutDTO workout){
        return workoutService.save(workout);
    }

    @GetMapping("/{workoutId}")
    public Workout getSet(@PathVariable UUID workoutId){
        return workoutService.findByID(workoutId);
    }
}
