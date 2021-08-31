package com.kifor.ProjectManager.DAO;

import com.kifor.ProjectManager.Entities.Projects.Project;
import com.kifor.ProjectManager.Entities.Task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Long> {

    Project findById(@Param("id") long id);
    Project findByName(@Param("name") String name);
    Project findByAccessCode(@Param("accessCode") String name);

    @Query(value = "insert into user_project values (?1,?2)", nativeQuery = true)
    void addUserToProject(long userId, long projectId);

    @Query(value = "select * from user_project up where userid = ?1 and projectid = ?2", nativeQuery = true)
    String connectionExist(long userId, long projectId);

    @Query(value = "select ue.id, ue.\"name\" from user_project up inner join user_entity ue on projectid = ?1 where ue.id = userid", nativeQuery = true)
    List<String> findAllProjectUsers(long projectId);

    @Query(value = "select * from task_entity where projectid = ?1", nativeQuery = true)
    List<String> findAllProjectTasks(long projectId);

    @Override
    Page<Project> findAll(Pageable pageable);
}
