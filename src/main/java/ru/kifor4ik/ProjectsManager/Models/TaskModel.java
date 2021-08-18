package ru.kifor4ik.ProjectsManager.Models;

import ru.kifor4ik.ProjectsManager.Entity.TaskEntity;

public class TaskModel {

    private String name;
    private String body;
    private Long projectId;

    public TaskModel(){ }

    public static TaskModel toModel(TaskEntity entity){
        TaskModel model = new TaskModel();

        model.projectId = entity.getProjectId();
        model.name = entity.getName();
        model.body = entity.getBody();
        return model;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
