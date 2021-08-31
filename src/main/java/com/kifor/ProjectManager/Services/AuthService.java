package com.kifor.ProjectManager.Services;

import com.kifor.ProjectManager.Configs.Security.JWT.JwtTokenProvider;
import com.kifor.ProjectManager.Entities.User.User;
import com.kifor.ProjectManager.Models.UserLoginModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;
    private UserService userService;

    @Autowired
    public AuthService(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, UserService userService){
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    public String login(UserLoginModel model){
        System.out.println("IM IN");
        try {
            System.out.println("IM IN TRY " + model.getEmail() + " " + model.getPassword());
            User user = userService.findByEmail(model.getEmail());
            if(user == null){
                throw new UsernameNotFoundException("Wrong email or password");
            }
            System.out.println("USER OK");

            System.out.println(model.getPassword());
            System.out.println(jwtTokenProvider.passwordEncoder().encode(model.getPassword()));
            System.out.println(user.getPassword());
            System.out.println(jwtTokenProvider.passwordEncoder().matches(model.getPassword(),user.getPassword()));
            if(jwtTokenProvider.passwordEncoder().matches(model.getPassword(),user.getPassword())) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(model.getEmail(), model.getPassword()));
            } else {
                return "Bad credentials";
            }


            System.out.println("AUTHENTICATE is OK");

            String token = jwtTokenProvider.generateToken(user.getEmail());
            System.out.println("Generate token ok");

            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
