package com.kifor.ProjectManager.Models;


import com.kifor.ProjectManager.Entities.User.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegistrationModel {

    private String name;
    private String email;
    private String password;

    public User toEntity(){
        User user = new User();
        user.setEmail(this.getEmail());
        user.setName(this.getName());
        user.setPassword(this.getPassword());
        return user;
    }
}
