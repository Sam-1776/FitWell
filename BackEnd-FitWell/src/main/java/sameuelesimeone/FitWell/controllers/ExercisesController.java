package sameuelesimeone.FitWell.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sameuelesimeone.FitWell.models.Exercise;
import sameuelesimeone.FitWell.services.ExerciseService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/exercise")
@CrossOrigin(origins = "http://localhost:4200")
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

    @GetMapping("/find")
    @PreAuthorize("hasAnyAuthority('USER', 'COACH', 'ADMIN')")
    public List<Exercise> getByName(@RequestParam String str){
        return exerciseService.findByNameContaining(str);
    }

    @GetMapping("/{exerciseId}")
    @PreAuthorize("hasAnyAuthority('USER', 'COACH', 'ADMIN')")
    public Exercise getExercise(@PathVariable UUID exerciseId){
        return exerciseService.findById(exerciseId);
    }
}
