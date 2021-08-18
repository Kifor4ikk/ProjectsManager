package ru.kifor4ik.ProjectsManager.Repository;

import org.springframework.data.repository.CrudRepository;
import ru.kifor4ik.ProjectsManager.Entity.UserEntity;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    UserEntity findByNickname(String name);
    UserEntity findByEmail(String email);
    UserEntity findByPassword(String password);
    List<UserEntity> findAll();
}
