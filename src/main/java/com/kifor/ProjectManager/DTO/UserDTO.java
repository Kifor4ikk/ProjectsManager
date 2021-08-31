package com.kifor.ProjectManager.DTO;

import com.kifor.ProjectManager.Entities.Status;
import com.kifor.ProjectManager.Entities.User.Roles;
import lombok.*;
import nonapi.io.github.classgraph.json.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String name;
    private String email;
    private Roles role;
    private Status status;
}
