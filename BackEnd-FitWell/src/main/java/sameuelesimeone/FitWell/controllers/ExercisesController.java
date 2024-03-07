package sameuelesimeone.FitWell.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sameuelesimeone.FitWell.models.Exercise;
import sameuelesimeone.FitWell.services.ExerciseService;

import java.util.UUID;

@RestController
@RequestMapping("/exercise")
public class ExercisesController {

    @Autowired
    ExerciseService exerciseService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'COACH', 'ADMIN')")
    public Page<Exercise> getAllExercises(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "id") String orderBy){
        return exerciseService.getExercises(page, size, orderBy);
    }

    @GetMapping("/{exerciseId}")
    @PreAuthorize("hasAnyAuthority('USER', 'COACH', 'ADMIN')")
    public Exercise getExercise(@PathVariable UUID exerciseId){
        return exerciseService.findById(exerciseId);
    }
}
