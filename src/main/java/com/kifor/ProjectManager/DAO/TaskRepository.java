package com.kifor.ProjectManager.DAO;

import com.kifor.ProjectManager.Entities.Task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task,Long> {

    Task findById(@Param("id") long id);
}
