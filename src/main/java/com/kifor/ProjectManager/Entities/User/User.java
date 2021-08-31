package com.kifor.ProjectManager.Entities.User;


import com.kifor.ProjectManager.Entities.Status;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_entity", schema = "project_manager")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotEmpty(message = "Name should not be empty")
    @Column(name = "name")
    private String name;

    @Email
    @Column(name = "email")
    private String email;

    @NotEmpty(message = "Password should not be empty")
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private Roles role;

    @Column(name = "status")
    private Status status;

    @Transient
    Set<String> listOfProjects = new HashSet<>();

}
