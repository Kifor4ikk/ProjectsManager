package ru.kifor4ik.ProjectsManager.Models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kifor4ik.ProjectsManager.Entity.UserEntity;

public class RegistrationModel {

    private String nickname;
    private String email;
    private String password;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static UserEntity toEntity(RegistrationModel model){

        UserEntity entity = new UserEntity();

        entity.setNickname(model.getNickname());
        entity.setEmail(model.getEmail());
        entity.setPassword(model.getPassword());

        return entity;
    }
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickName) {
        this.nickname = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
