package com.kifor.ProjectManager.Services;


import com.kifor.ProjectManager.DAO.ProjectRepository;
import com.kifor.ProjectManager.DAO.UserRepository;
import com.kifor.ProjectManager.DTO.UserDTO;
import com.kifor.ProjectManager.Entities.Projects.Project;
import com.kifor.ProjectManager.Entities.Status;
import com.kifor.ProjectManager.Entities.User.Roles;
import com.kifor.ProjectManager.Entities.User.User;
import com.kifor.ProjectManager.Exceptions.UserAlreadyExistException;
import com.kifor.ProjectManager.Mappers.UserMapper;
import com.kifor.ProjectManager.Models.UserLoginModel;
import com.kifor.ProjectManager.Models.UserRegistrationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     *
     * @param user user registration model to create or edit user
     * @return positive msg if ok, bad message if something wrong
     * @throws UserAlreadyExistException
     */
    public String newUser(UserRegistrationModel user) throws UserAlreadyExistException {
        if(userRepository.findByName(user.getName()) != null)
            return "User with current name already exist!";

        if(userRepository.findByEmail(user.getEmail()) != null)
            return "User with current email already exist!";

        User userEntity = user.toEntity();
        userEntity.setRole(Roles.USER);
        userEntity.setStatus(Status.ACTIVE);
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(userEntity);
        return user.getEmail() + "Registered!";
    }

    public String joinToProjectByAccessCode(UserLoginModel userModel,String accessCode){
        Project project = projectRepository.findByAccessCode(accessCode);
        User user = userRepository.findByEmail(userModel.getEmail());
        if(project == null) return "Bad access code";
        if(user == null && passwordEncoder.matches(user.getPassword(), userModel.getPassword())) return "User not found";
        try{
            if(projectRepository.connectionExist(user.getId(), project.getId()) == null){
                projectRepository.addUserToProject(user.getId(), project.getId());
            } else {
                return "User already in this project";
            }
        } catch (Exception e){
        }
        return "User " + user.getName() + " joined to project " + project.getName();
    }

    public String edit(UserRegistrationModel userModel){
        User user = userRepository.findByEmail(userModel.getEmail());
        if(user == null)
            return "User with current email not exist!";

        if(userRepository.findByName(user.getName()) != null)
            return "User with current name already exist!";

        if(passwordEncoder.matches(user.getPassword(), userModel.getPassword())){
            user.setName(userModel.getName());
            userRepository.save(user);
        } else{
            return "Wrong password!";
        }
        return "Successfully changed!";
    }

    public String delete(long id){
        User user = userRepository.findById(id);
        if(user != null){
            user.setStatus(Status.DELETED);
        }
        return user.getEmail() + " was deleted!";
    }

    public String ban(long id){
        User user = userRepository.findById(id);
        if(user != null){
            user.setStatus(Status.BANNED);
        }
        return user.getEmail() + " was banned!";
    }

    public User findById(long id){;
        User user = userRepository.findById(id);
        user.setListOfProjects(userRepository.findAllUserProjects(user.getId()));
        return user;
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Page<User> getAll(int page, int size){
        final Pageable pageable = PageRequest.of(page, size);

//        List<User> list = userRepository.findAll(pageable).stream()
//                .map(userMapper::toDTO)
//                .collect(Collectors.toList());

        List<User> list = userRepository.findAll(pageable).stream()
                .collect(Collectors.toList());

        for(User user : list){
            user = findById(user.getId());
        }
        return new PageImpl<User>(list, pageable, list.size());
    }


    public UserDTO toDTO(User user){
        UserDTO model = new UserDTO();
        model.setName(user.getName());
        model.setEmail(user.getEmail());
        model.setStatus(user.getStatus());
        model.setRole(user.getRole());
        return model;
    }
}
