package sameuelesimeone.FitWell.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sameuelesimeone.FitWell.dto.SetDTO;
import sameuelesimeone.FitWell.models.Set;
import sameuelesimeone.FitWell.services.SetService;

import java.util.UUID;

@RestController
@RequestMapping("/set")
public class SetController {

    @Autowired
    SetService setService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Set create(@RequestBody SetDTO set){
        return setService.save(set);
    }

    @GetMapping("/{setId}")
    public Set getSet(@PathVariable UUID setId){
        return setService.findById(setId);
    }
}
