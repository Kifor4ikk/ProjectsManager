package com.kifor.ProjectManager.Entities.Projects;

import com.kifor.ProjectManager.Entities.Status;
import com.kifor.ProjectManager.Entities.Task.Task;
import com.kifor.ProjectManager.Entities.User.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "project_entity", schema = "project_manager")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotEmpty(message = "Name should not be empty")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "Name should not be empty")
    @Column(name = "accesscode")
    private String accessCode;

    @NotNull
    @Column(name = "adminid")
    private long adminId;

    @NotNull
    @Column(name = "status")
    private Status status;

    @Transient
    private List<String> userList = new ArrayList<>();
    @Transient
    private Set<Task> taskList = new HashSet<>();
}
