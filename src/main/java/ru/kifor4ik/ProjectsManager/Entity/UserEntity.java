package ru.kifor4ik.ProjectsManager.Entity;


import org.springframework.security.core.authority.SimpleGrantedAuthority;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "user_entity")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2,max = 25, message = "Name should be between 2 and 25 letters")
    private String nickname;

    @Email
    private String email;

    @NotEmpty(message = "Password should not be empty")
    private String password;

    private Roles role;

    @ManyToMany
    @JoinTable(
            name = "user_projects",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "project_id")}
    )
    private Set<ProjectEntity> projects = new HashSet<>();

    private Status UserStatus;

    public UserEntity(){}

    public Set<ProjectEntity> getProjects() {
        return projects;
    }

    public void setProjects(Set<ProjectEntity> projects) {
        this.projects = projects;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public Status getUserStatus() {
        return UserStatus;
    }

    public void setUserStatus(Status userStatus) {
        UserStatus = userStatus;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }
}
