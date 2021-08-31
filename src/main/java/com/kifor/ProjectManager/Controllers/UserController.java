package com.kifor.ProjectManager.Controllers;

import com.kifor.ProjectManager.DTO.UserDTO;
import com.kifor.ProjectManager.Entities.User.User;
import com.kifor.ProjectManager.Exceptions.UserAlreadyExistException;
import com.kifor.ProjectManager.Models.UserLoginModel;
import com.kifor.ProjectManager.Models.UserRegistrationModel;
import com.kifor.ProjectManager.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public String newUser(@RequestBody UserRegistrationModel userModel) throws UserAlreadyExistException {
        return userService.newUser(userModel);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.findById(id);
    }

    @PutMapping
    public String editUser(@RequestBody UserRegistrationModel userModel) throws UserAlreadyExistException {
        return userService.edit(userModel);
    }

    @PostMapping("/join")
    public String joinToProject(@RequestBody UserLoginModel model, @RequestParam String accessCode){
        return userService.joinToProjectByAccessCode(model,accessCode);
    }

    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    @DeleteMapping
    public String deleteUser(@RequestParam long id){
        return userService.delete(id);
    }

    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    @GetMapping("/ban")
    public String banUser(@RequestParam long id){
        return userService.ban(id);
    }

    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    @GetMapping("/all")
    public Page<User> getAll(@RequestParam(defaultValue = "0",required = false) int page,
                             @RequestParam(defaultValue = "5",required = false) int size){
        return userService.getAll(page, size);
    }
}
