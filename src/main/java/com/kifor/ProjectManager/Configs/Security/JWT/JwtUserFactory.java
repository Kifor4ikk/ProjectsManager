package com.kifor.ProjectManager.Configs.Security.JWT;

import com.kifor.ProjectManager.Entities.User.User;

public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser toJwt(User user){
        return new JwtUser(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus(),
                user.getPassword()
        );
    }
}
