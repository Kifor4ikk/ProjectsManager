package com.kifor.ProjectManager.Controllers;

import com.kifor.ProjectManager.Configs.Security.JWT.JwtTokenProvider;
import com.kifor.ProjectManager.Entities.Projects.Project;
import com.kifor.ProjectManager.Models.UserLoginModel;
import com.kifor.ProjectManager.Models.UserRegistrationModel;
import com.kifor.ProjectManager.Services.AuthService;
import com.kifor.ProjectManager.Services.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public String login(@RequestBody UserLoginModel model){
        System.out.println("ZASHLO!!!");
        return authService.login(model);
    }

    @GetMapping("/")
    public String test2(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities() + " " + auth.getName();
    }

}
