package sameuelesimeone.FitWell.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sameuelesimeone.FitWell.dto.SetDTO;
import sameuelesimeone.FitWell.models.Set;
import sameuelesimeone.FitWell.services.SetService;

import java.util.UUID;

@RestController
@RequestMapping("/set")
@CrossOrigin(origins = "http://localhost:4200")
public class SetController {

    @Autowired
    SetService setService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('USER', 'COACH')")
    public Set create(@RequestBody SetDTO set){
        return setService.save(set);
    }

    @GetMapping("/{setId}")
    @PreAuthorize("hasAnyAuthority('USER', 'COACH')")
    public Set getSet(@PathVariable UUID setId){
        return setService.findById(setId);
    }

    @PatchMapping("/{setId}")
    @PreAuthorize("hasAnyAuthority('USER', 'COACH', 'ADMIN')")
    public Set modSet(@PathVariable UUID setId, @RequestBody SetDTO set){
        return setService.modSet(setId, set);
    }

    @DeleteMapping("/{setId}")
    @PreAuthorize("hasAnyAuthority('USER', 'COACH', 'ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSet(@PathVariable UUID setId){
        setService.deleteSet(setId);
    }
}
