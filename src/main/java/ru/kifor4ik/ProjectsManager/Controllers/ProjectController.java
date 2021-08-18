package ru.kifor4ik.ProjectsManager.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kifor4ik.ProjectsManager.Entity.ProjectEntity;
import ru.kifor4ik.ProjectsManager.Entity.TaskEntity;
import ru.kifor4ik.ProjectsManager.Exeptions.ProjectAlreadyExistException;
import ru.kifor4ik.ProjectsManager.Exeptions.ProjectNotFoundException;
import ru.kifor4ik.ProjectsManager.Models.ProjectModel;
import ru.kifor4ik.ProjectsManager.Service.ProjectService;
import ru.kifor4ik.ProjectsManager.Service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    public ProjectService projectService;


    @PostMapping
    public ResponseEntity newProject(@RequestBody ProjectModel model){

        try {
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
    public ResponseEntity getProject(@PathVariable Long id){

        try {

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
    public ResponseEntity getAllProjects(){
        return ResponseEntity.ok().body(projectService.getAll());
    }
}
