package ru.kifor4ik.ProjectsManager.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.kifor4ik.ProjectsManager.Entity.TaskEntity;

public interface TaskRepository extends PagingAndSortingRepository<TaskEntity, Long> {

    Page<TaskEntity> findAll(Pageable pageable);
}
