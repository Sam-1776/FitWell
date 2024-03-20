package sameuelesimeone.FitWell.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sameuelesimeone.FitWell.dto.SetDTO;
import sameuelesimeone.FitWell.dto.WorkoutDTO;
import sameuelesimeone.FitWell.models.Set;
import sameuelesimeone.FitWell.models.Workout;
import sameuelesimeone.FitWell.services.WorkoutService;

import java.util.UUID;

@RestController
@RequestMapping("/workout")
@CrossOrigin(origins = "http://localhost:4200")
public class WorkoutController {

    @Autowired
    WorkoutService workoutService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('USER', 'COACH')")
    public Workout create(@RequestBody WorkoutDTO workout){
        return workoutService.save(workout);
    }

    @GetMapping("/{workoutId}")
    @PreAuthorize("hasAnyAuthority('USER', 'COACH')")
    public Workout getWorkout(@PathVariable UUID workoutId){
        return workoutService.findByID(workoutId);
    }

    @PatchMapping("/{workoutId}")
    @PreAuthorize("hasAnyAuthority('USER', 'COACH', 'ADMIN')")
    public Workout modWorkout(@PathVariable UUID workoutId, @RequestBody WorkoutDTO workout){
        return workoutService.modWorkout(workoutId, workout);
    }

    @DeleteMapping("/{workoutId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('USER', 'COACH', 'ADMIN')")
    public void deleteWorkout(@PathVariable UUID workoutId){
        workoutService.deleteWorkout(workoutId);
    }
}
