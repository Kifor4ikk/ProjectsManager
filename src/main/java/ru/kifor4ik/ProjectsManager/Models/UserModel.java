package ru.kifor4ik.ProjectsManager.Models;

import ru.kifor4ik.ProjectsManager.Entity.ProjectEntity;
import ru.kifor4ik.ProjectsManager.Entity.Roles;
import ru.kifor4ik.ProjectsManager.Entity.Status;
import ru.kifor4ik.ProjectsManager.Entity.UserEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserModel {

    private Long id;
    private String Nickname;

    private List<ProjectModel> projectList;

    private Roles role;
    private Status status;

    public UserModel(){};

    public static UserModel toModel(UserEntity entity){
        UserModel model = new UserModel();
        List<ProjectModel> projectList = new ArrayList<>();
        model.setId(entity.getId());
        model.setNickname(entity.getNickname());
        model.setRole(entity.getRole());
        model.setStatus(entity.getUserStatus());
        Iterator<ProjectEntity> iterator = entity.getProjects().iterator();
        while(iterator.hasNext()){
            ProjectEntity projectModel = iterator.next();
            if(projectModel.getProjectStatus().equals(Status.ACTIVE)){
                projectList.add(ProjectModel.toModel(projectModel));
            }
        }
        model.setProjectList(projectList);
        return model;
    }


    public List<ProjectModel> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<ProjectModel> projectList) {
        this.projectList = projectList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }
}
