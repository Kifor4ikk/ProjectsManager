package ru.kifor4ik.ProjectsManager.Models;

import ru.kifor4ik.ProjectsManager.Entity.Status;
import ru.kifor4ik.ProjectsManager.Entity.TaskEntity;


public class TaskModel {

    private String name;
    private String body;
    private Long projectId;

    private Status taskStatus;

    public TaskModel(){ }

    public static TaskModel toModel(TaskEntity entity){
        TaskModel model = new TaskModel();

        model.setProjectId(entity.getProjectId());
        model.setName(entity.getName());
        model.setBody(entity.getBody());
        model.setTaskStatus(entity.getTaskStatus());

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

    public Status getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Status taskStatus) {
        this.taskStatus = taskStatus;
    }
}
