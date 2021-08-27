package ru.kifor4ik.ProjectsManager.Service;

import com.sun.xml.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kifor4ik.ProjectsManager.Configs.Security.Security;
import ru.kifor4ik.ProjectsManager.Entity.ProjectEntity;
import ru.kifor4ik.ProjectsManager.Entity.Roles;
import ru.kifor4ik.ProjectsManager.Entity.Status;
import ru.kifor4ik.ProjectsManager.Exeptions.ProjectNotFoundException;
import ru.kifor4ik.ProjectsManager.Exeptions.UserAlreadyExistExeption;
import ru.kifor4ik.ProjectsManager.Exeptions.UserNotFoundExeption;
import ru.kifor4ik.ProjectsManager.Entity.UserEntity;
import ru.kifor4ik.ProjectsManager.Exeptions.WrongStatusCodeException;
import ru.kifor4ik.ProjectsManager.Models.RegistrationModel;
import ru.kifor4ik.ProjectsManager.Models.UserModel;
import ru.kifor4ik.ProjectsManager.Repository.ProjectRepository;
import ru.kifor4ik.ProjectsManager.Repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
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

    /**
     *
     * @param user UserEntity with params Name, Email, password
     * @return new User
     * @throws UserAlreadyExistExeption user with current name, email already exist
     */
    public UserEntity registration(RegistrationModel user) throws UserAlreadyExistExeption {


        if (userRepository.findByNickname(user.getNickname()) != null) {
            throw new UserAlreadyExistExeption("User with this name already exist");
        }
        if(userRepository.findByEmail(user.getEmail()) != null){
            throw new UserAlreadyExistExeption("User with this Email already exist");
        }
        UserEntity userEntity = RegistrationModel.toEntity(user);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setRole(Roles.USER);
        userEntity.setUserStatus(Status.ACTIVE);
        return userRepository.save(userEntity);
    }

    /**
     *
     * @param id user id
     * @return User if was found
     * @throws UserNotFoundExeption - user not found :|
     */
    public UserEntity findById(Long id) throws UserNotFoundExeption {

        try {
            UserEntity user = userRepository.findById(id).get();

            if(user.getUserStatus().equals(Status.DELETED) || user.getUserStatus().equals(Status.BANNED)){
                throw new UserNotFoundExeption("User not found");
            }
            return user;

        } catch (NoSuchElementException e) {
            throw new UserNotFoundExeption("User not found");
        }
    }

    /**
     *
     * @param email user Email which was used for create profile
     * @return userEntity with current email
     * @throws UserNotFoundExeption - user not found
     */
    public UserEntity findByEmail(String email) throws UserNotFoundExeption {

        UserEntity user = userRepository.findByEmail(email);
        if(user == null || user.getUserStatus().equals(Status.DELETED) || user.getUserStatus().equals(Status.BANNED)) {
            throw new UserNotFoundExeption("User with current mail not found");
        }
        return user;
    }

    /**
     *
     * @param id user id who must be updated
     * @param entityUpdate new UserEntity with new params
     * @return
     * @throws UserNotFoundExeption
     * @throws UserAlreadyExistExeption
     */
    public UserEntity editUser(Long id, UserEntity entityUpdate) throws UserNotFoundExeption, UserAlreadyExistExeption {

        if (this.findById(id) != null && id == entityUpdate.getId() &&
                !entityUpdate.getUserStatus().equals(Status.DELETED) && !entityUpdate.getUserStatus().equals(Status.BANNED)) {

            if (userRepository.findByNickname(entityUpdate.getNickname()) != null) {
                throw new UserAlreadyExistExeption("User with current name already exist!");
            }
            userRepository.save(entityUpdate);
            return entityUpdate;

        } else {
            throw new UserNotFoundExeption("User not found or not valid Id");
        }
    }

    /**
     * OnlyAdmins
     * Func for user who has admin authority
     * @return list of ALL PROJECTS
     */
    public List getAll(Pageable pageable) {

        List<UserModel> list = new ArrayList<>();

        for (UserEntity entity : userRepository.findAll(pageable)) {
            list.add(UserModel.toModel(entity));
        }

        return list;
    }

    /**
     * Join to project by access code
     * @param userId user who want to join
     * @param accessCode of project
     * @return true if user successfully join
     * @throws ProjectNotFoundException - project with Current code not found
     * @throws UserNotFoundExeption - user who want to join not found
     */
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

    public boolean giveAdminRights(String email) throws UserNotFoundExeption {
        UserEntity user = userRepository.findByEmail(email);
        if(user == null) {
            throw new UserNotFoundExeption("User with current mail not found");
        }
        user.setRole(Roles.ADMIN);
        userRepository.save(user);
        return true;
    }

    public boolean setProfileStatus(String email, int status) throws UserNotFoundExeption, WrongStatusCodeException {
        UserEntity user = userRepository.findByEmail(email);
        if(user == null) {
            throw new UserNotFoundExeption("User with current mail not found");
        }
        if(status < 0 && status > 2){
            throw new WrongStatusCodeException("Wrong status code");
        }


        switch (status){
            case 0: {
                user.setUserStatus(Status.ACTIVE);
                break;
            }
            case 1:{
                user.setUserStatus(Status.DELETED);

                break;
            }
            case 2:{
                user.setUserStatus(Status.BANNED);
                break;
            }
        }

        userRepository.save(user);
        return true;
    }
}
