package ru.kifor4ik.ProjectsManager.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ru.kifor4ik.ProjectsManager.Entity.ProjectEntity;

import java.util.List;

public interface ProjectRepository extends PagingAndSortingRepository<ProjectEntity, Long> {

    ProjectEntity findByName(@Param("name") String name);
    ProjectEntity findByAccessCode(@Param("access_code") String code);
    Page<ProjectEntity> findAll(Pageable pageable);

}
