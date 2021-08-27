package ru.kifor4ik.ProjectsManager.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ru.kifor4ik.ProjectsManager.Entity.UserEntity;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

    UserEntity findByNickname(@Param("nickname") String name);
    UserEntity findByEmail(@Param("email") String email);
    Page<UserEntity> findAll(Pageable pageable);
}
