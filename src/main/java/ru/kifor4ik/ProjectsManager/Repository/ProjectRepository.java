package ru.kifor4ik.ProjectsManager.Repository;

import org.springframework.data.repository.CrudRepository;
import ru.kifor4ik.ProjectsManager.Entity.ProjectEntity;

import java.util.List;

public interface ProjectRepository extends CrudRepository <ProjectEntity, Long> {

    ProjectEntity findByName(String name);
    ProjectEntity findByAccessCode(String code);
    List<ProjectEntity> findAll();

}
