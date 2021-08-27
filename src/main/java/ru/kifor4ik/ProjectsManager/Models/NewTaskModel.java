package ru.kifor4ik.ProjectsManager.Models;

import org.springframework.beans.factory.annotation.Autowired;
import ru.kifor4ik.ProjectsManager.Entity.TaskEntity;
import ru.kifor4ik.ProjectsManager.Exeptions.ProjectNotFoundException;
import ru.kifor4ik.ProjectsManager.Service.ProjectService;

public class NewTaskModel {

    private String name;
    private String body;
    private Long projectId;

    public NewTaskModel(){ }

    public static TaskEntity toEntity(NewTaskModel model) throws ProjectNotFoundException {
        TaskEntity entity = new TaskEntity();

        entity.setName(model.getName());
        entity.setBody(model.getBody());
        entity.setProjectId(model.getProjectId());
        return entity;
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
