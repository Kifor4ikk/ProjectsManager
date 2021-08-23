package ru.kifor4ik.ProjectsManager.Controllers;


import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.kifor4ik.ProjectsManager.Entity.ProjectEntity;
import ru.kifor4ik.ProjectsManager.Entity.TaskEntity;
import ru.kifor4ik.ProjectsManager.Entity.UserEntity;
import ru.kifor4ik.ProjectsManager.Exeptions.ProjectAlreadyExistException;
import ru.kifor4ik.ProjectsManager.Exeptions.ProjectNotFoundException;
import ru.kifor4ik.ProjectsManager.Exeptions.UserNotFoundExeption;
import ru.kifor4ik.ProjectsManager.Models.ProjectModel;
import ru.kifor4ik.ProjectsManager.Service.ProjectService;
import ru.kifor4ik.ProjectsManager.Service.TaskService;
import ru.kifor4ik.ProjectsManager.Service.UserService;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    public ProjectService projectService;
    @Autowired
    public UserService userService;

    @PostMapping
    public ResponseEntity newProject(@RequestBody ProjectModel model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            model.setAdminId(userService.findByEmail(auth.getName()).getId());
            projectService.newProject(model);
            return ResponseEntity.ok(model.getName() + " created!");

        } catch (ProjectAlreadyExistException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Oops.. Something wrong..");
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity getProject(@PathVariable Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        try {
            UserEntity user = userService.findByEmail(auth.getName());
            if(!user.getProjects().contains(projectService.findById(id)) || auth.getAuthorities().contains("ADMIN")){
                return ResponseEntity.badRequest().body("403 FORBIDDEN");
            }
            return ResponseEntity.ok(ProjectModel.toModel(projectService.findById(id)));
        } catch (ProjectNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Oops.. Something wrong..");
        }
    }

    @PutMapping
    public ResponseEntity editProject(@RequestParam Long id, @RequestBody ProjectModel projectModel){

        try{

            ProjectEntity project = projectService.editProject(id, projectModel);
            if(userService.findById(project.getAdminId()).getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()) ||
            SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains("ADMIN")){
                return ResponseEntity.badRequest().body("403 FORBIDEN");
            }
            return ResponseEntity.ok(project.getId() + " was successfully changed!");
        } catch (ProjectNotFoundException e ){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ProjectAlreadyExistException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Oops... Something wrong");
        }
    }

    @DeleteMapping
    public ResponseEntity deleteProject(@RequestParam Long id){

        try{

            return ResponseEntity.ok(projectService.deleteProject(id).getId() + " was deleted");
        } catch (ProjectNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity getAllProjects() throws UserNotFoundExeption {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("TEST TEST TEST");
        System.out.println(auth.getAuthorities());
        return ResponseEntity.ok().body(projectService.getAll());
    }
}
