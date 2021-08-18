package ru.kifor4ik.ProjectsManager.Repository;

import org.springframework.data.repository.CrudRepository;
import ru.kifor4ik.ProjectsManager.Entity.TaskEntity;

import java.util.List;

public interface TaskRepository extends CrudRepository<TaskEntity, Long> {

}
