package ru.kifor4ik.ProjectsManager.Entity;


import ru.kifor4ik.ProjectsManager.Models.ProjectModel;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Entity
@Table(name = "project_entity")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private Long adminId;

    @NotEmpty(message = "Project name should be not empty")
    @Size(min = 4, max = 35, message = "Name should be between 4 and 35 letters")
    private String name;

    @NotEmpty(message = "AccessCode should be not empty")
    @Size(min = 10, max = 10, message = "Access code should be 10 symbols")
    private String accessCode;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    public List<TaskEntity> taskList;

    @ManyToMany
    @JoinTable(
            name = "user_projects",
            joinColumns = { @JoinColumn(name = "project_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id")}
    )
    public Set<UserEntity> users = new HashSet<>();

    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }

    public static ProjectEntity fromModel(ProjectModel model){
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.name = model.getName();
        projectEntity.adminId = model.getAdminId();
        return projectEntity;
    }


    public static String generateCode(){
        int count = 10;
        String code = "";
        boolean valid = false;
        Random rand = new Random();
        for(int i = 0; i < count; i++){
            valid = false;
            while(valid != true) {

                switch (rand.nextInt(3)) {
                    case 0: {
                        code += (char) (rand.nextInt(10) + 48);
                        valid = true;
                        break;
                    }
                    case 1: {
                        code += (char) (rand.nextInt(26) + 65);
                        valid = true;
                        break;
                    }
                    case 2: {
                        code += (char) (rand.nextInt(26) + 97);
                        valid = true;
                        break;
                    }
                }
            }
        }
        return code;
    }

    public ProjectEntity() { }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
}
