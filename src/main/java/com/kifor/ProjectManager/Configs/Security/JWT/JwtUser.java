package com.kifor.ProjectManager.Configs.Security.JWT;

import com.kifor.ProjectManager.Entities.Status;
import com.kifor.ProjectManager.Entities.User.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JwtUser implements UserDetails {

    private Long id;
    private String name;
    private String email;
    private Roles role;
    private Status status;
    private String password;
    private List<GrantedAuthority> roles = new ArrayList<>();

    public JwtUser(Long id, String name, String email, Roles role, Status status, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.status = status;
        this.password = password;
        this.roles.add(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if(this.status == Status.ACTIVE){
            return true;
        } else {
            return false;
        }
    }
}
