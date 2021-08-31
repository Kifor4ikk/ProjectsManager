package com.kifor.ProjectManager.DAO;


import com.kifor.ProjectManager.Entities.User.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface UserRepository extends JpaRepository<User,Long> {

    User findById(@Param("id") long id);
    User findByName(@Param("name") String name);
    User findByEmail(@Param("email") String email);

    @Query(value = "select pe.id, pe.\"name\" from user_project up inner join project_entity pe on userid = ?1 where pe.id = projectid and pe.status = 0 ",nativeQuery = true)
    Set<String> findAllUserProjects(@Param("projectid") long projectId);

    @Override
    List<User> findAll();
}
