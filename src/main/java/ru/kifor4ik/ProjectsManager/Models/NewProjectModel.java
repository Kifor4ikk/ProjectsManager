package ru.kifor4ik.ProjectsManager.Models;

import ru.kifor4ik.ProjectsManager.Entity.ProjectEntity;

public class NewProjectModel {

    private String name;

    public static ProjectEntity toEntity(NewProjectModel model){
        ProjectEntity entity = new ProjectEntity();

        entity.setName(model.getName());

        return entity;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
