package ru.kifor4ik.ProjectsManager.Models;


import ru.kifor4ik.ProjectsManager.Entity.ProjectEntity;
import ru.kifor4ik.ProjectsManager.Entity.TaskEntity;
import ru.kifor4ik.ProjectsManager.Entity.UserEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProjectModel {

    private Long id;
    private Long adminId;
    private String name;
    private String accessCode;

    private List<TaskModel> listOfTasks;

    private List<String> listOfUsers;

    public static ProjectModel toModel(ProjectEntity entity){

        ProjectModel model = new ProjectModel();
        List<TaskModel> listOfTasks = new ArrayList<>();
        List<String> listOfUsers = new ArrayList<>();
        model.setAdminId(entity.getAdminId());
        model.setName(entity.getName());
        model.setId(entity.getId());
        model.setAccessCode(entity.getAccessCode());

        for (TaskEntity task : entity.taskList){
            listOfTasks.add(TaskModel.toModel(task));
        }
        model.setListOfTasks(listOfTasks);

        Iterator<UserEntity> iterator = entity.getUsers().iterator();
        while(iterator.hasNext()){

            UserEntity test = iterator.next();
            listOfUsers.add(test.getId() + ":" + test.getNickname());
        }

        model.setListOfUsers(listOfUsers);
        return model;
    }

    public ProjectModel(){
    }

    public List<TaskModel> getListOfTasks() {
        return listOfTasks;
    }

    public void setListOfTasks(List<TaskModel> listOfTasks) {
        this.listOfTasks = listOfTasks;
    }

    public List<String> getListOfUsers() {
        return listOfUsers;
    }

    public void setListOfUsers(List<String> listOfUsers) {
        this.listOfUsers = listOfUsers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
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
}
