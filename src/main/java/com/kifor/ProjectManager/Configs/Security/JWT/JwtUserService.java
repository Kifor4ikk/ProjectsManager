package com.kifor.ProjectManager.Configs.Security.JWT;

import com.kifor.ProjectManager.Entities.User.User;
import com.kifor.ProjectManager.Services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtUserService implements UserDetailsService {

    private UserService userService;

    @Autowired
    public JwtUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userEntity = userService.findByEmail(email);
        if(userEntity == null) throw new UsernameNotFoundException("User with email: " + email + " not found");

        JwtUser user = JwtUserFactory.toJwt(userEntity);
        log.info("In load by " + userEntity.getEmail());
        return user;
    }
}
