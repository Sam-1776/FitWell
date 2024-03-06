package sameuelesimeone.FitWell.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sameuelesimeone.FitWell.dto.LoginDTO;
import sameuelesimeone.FitWell.dto.UserDTO;
import sameuelesimeone.FitWell.dto.UserLoginDTO;
import sameuelesimeone.FitWell.exceptions.BadRequestException;
import sameuelesimeone.FitWell.models.User;
import sameuelesimeone.FitWell.services.AuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public LoginDTO login(@RequestBody UserLoginDTO body) {
        return authService.authUserAndGenerateToken(body);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody @Validated UserDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return this.authService.save(body);
    }
}
