package ru.kifor4ik.ProjectsManager.Service;

import com.sun.xml.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kifor4ik.ProjectsManager.Configs.Security.Security;
import ru.kifor4ik.ProjectsManager.Entity.ProjectEntity;
import ru.kifor4ik.ProjectsManager.Exeptions.ProjectNotFoundException;
import ru.kifor4ik.ProjectsManager.Exeptions.UserAlreadyExistExeption;
import ru.kifor4ik.ProjectsManager.Exeptions.UserNotFoundExeption;
import ru.kifor4ik.ProjectsManager.Entity.UserEntity;
import ru.kifor4ik.ProjectsManager.Models.UserModel;
import ru.kifor4ik.ProjectsManager.Repository.ProjectRepository;
import ru.kifor4ik.ProjectsManager.Repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity registration(UserEntity user) throws UserAlreadyExistExeption {

        if (userRepository.findByNickname(user.getNickname()) != null) {
            throw new UserAlreadyExistExeption("User with this name already exist");
        }
        if(userRepository.findByEmail(user.getEmail()) != null){
            System.out.println("EXIST");
            throw new UserAlreadyExistExeption("User with this Email already exist");
        }


        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }


    public UserEntity findById(Long id) throws UserNotFoundExeption {

        try {
            UserEntity user = userRepository.findById(id).get();
            return user;

        } catch (NoSuchElementException e) {
            throw new UserNotFoundExeption("User not found");
        }
    }

    public UserEntity findByEmail(String email) throws UserNotFoundExeption {

        UserEntity user = userRepository.findByEmail(email);
        if(user == null) {
            throw new UserNotFoundExeption("User with current mail not found");
        }
        return user;
    }

    public UserEntity editUser(Long id, UserEntity entityUpdate) throws UserNotFoundExeption, UserAlreadyExistExeption {

        if (this.findById(id) != null && id == entityUpdate.getId()) {

            if (userRepository.findByNickname(entityUpdate.getNickname()) != null) {
                throw new UserAlreadyExistExeption("User with current name already exist!");
            }
            userRepository.save(entityUpdate);
            return entityUpdate;


        } else {
            throw new UserNotFoundExeption("User not found or not valid Id");
        }
    }

    public UserModel delete(Long id) throws UserNotFoundExeption {

        UserModel model = UserModel.toModel(this.findById(id));
        if (model != null) {
            userRepository.deleteById(id);
        }
        return model;
    }


    public List<UserModel> getAll() {

        List<UserModel> list = new ArrayList<>();

        for (UserEntity entity : userRepository.findAll()) {
            list.add(UserModel.toModel(entity));
        }

        return list;
    }

    public boolean joinToProject(Long userId, String accessCode) throws ProjectNotFoundException, UserNotFoundExeption {

        UserEntity user = userRepository.findById(userId).get();
        ProjectEntity projectEntity = projectRepository.findByAccessCode(accessCode);
        if (user != null && projectEntity != null) {
            user.getProjects().add(projectEntity);
            userRepository.save(user);
            projectRepository.save(projectEntity);
            return true;
        } else {
            if (user == null) {
                throw new UserNotFoundExeption("Bad user");
            }
            if (projectEntity == null) {
                throw new ProjectNotFoundException("Bad access code");
            }
        }

        return false;
    }
}
