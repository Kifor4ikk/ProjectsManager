package ru.kifor4ik.ProjectsManager.Controllers;


import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import ru.kifor4ik.ProjectsManager.Models.NewProjectModel;
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
    public ResponseEntity newProject(@RequestBody NewProjectModel model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {

            projectService.newProject(model, "test2@test");
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
//            if(!userService.findByEmail(auth.getName()).getProjects().contains(projectService.findById(id)) || auth.getAuthorities().contains("ADMIN")){
//                return ResponseEntity.badRequest().body("403 FORBIDDEN");
//            }
            return ResponseEntity.ok(ProjectModel.toModel(projectService.findById(id)));
        } catch (ProjectNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Oops.. Something wrong..");
        }
    }

    @PutMapping
    public ResponseEntity editProject(@RequestParam Long id, @RequestBody NewProjectModel projectModel){

        try{

            ProjectEntity project = projectService.editProject(id, projectModel);
            if(userService.findById(project.getAdminId()).getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()) ||
            SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains("ADMIN")){
                return ResponseEntity.badRequest().body("403 FORBIDDEN");
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
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity deleteProject(@RequestParam Long id){

        try{
            projectService.deleteProject(id);
            return ResponseEntity.ok(id + "# project was deleted");
        } catch (ProjectNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity getAllProjects(@PageableDefault(sort = {"id"},direction = Sort.Direction.DESC, page = 0,size = 5,value = 5) Pageable pageable,
                                         @RequestParam(required = false) long size, @RequestParam(required = false) long page) throws UserNotFoundExeption {
        return ResponseEntity.ok().body(projectService.getAll(pageable));
    }
}
