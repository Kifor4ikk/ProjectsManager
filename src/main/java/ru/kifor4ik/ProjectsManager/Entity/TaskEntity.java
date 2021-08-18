package ru.kifor4ik.ProjectsManager.Entity;

import ru.kifor4ik.ProjectsManager.Models.TaskModel;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Task name should be not empty")
    @Size(min = 3, max = 30, message = "Task name should be between 3 and 30 letters")
    private String name;

    @NotEmpty(message = "Task body should be not empty")
    @Size(min = 20, max = 10000, message = "Task body should be between 20 and 10000 symbols")
    private String body;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    public TaskEntity(){}

    public static TaskEntity fromModel(ProjectEntity projectEntity, TaskModel model){
        TaskEntity entity = new TaskEntity();
        entity.project = projectEntity;
        entity.name = model.getName();
        entity.body = model.getBody();

        return entity;
    }
    public Long getProjectId(){
        return this.project.getId();
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
