package sameuelesimeone.FitWell.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sameuelesimeone.FitWell.dto.RoleDTO;
import sameuelesimeone.FitWell.models.User;
import sameuelesimeone.FitWell.services.UserService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER','ADMIN', 'COACH')")
    public List<User> getAll() {
        return this.userService.getUsers();
    }

    @GetMapping("/profile")
    public User getProfile(@AuthenticationPrincipal User currentUser) {
        return currentUser;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id) {
        return this.userService.findById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User uploadUser(@PathVariable UUID id, @RequestBody User updateUser) {
        return this.userService.update(id, updateUser);
    }

    @PutMapping("/profile")
    public User updateMyProfile(@AuthenticationPrincipal User currentUser, @RequestBody User updateUser) {
        return this.uploadUser(currentUser.getId(), updateUser);
    }

    @DeleteMapping("/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMySelf(@AuthenticationPrincipal User currentUser) {
        this.userService.deleteUser(currentUser.getId());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID id) {
        this.userService.deleteUser(id);
    }


    @PatchMapping("/{UserId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User updateRole(@PathVariable UUID UserId, @RequestBody RoleDTO role) throws Exception {
        return this.userService.updateRole(UserId, role);
    }
}
