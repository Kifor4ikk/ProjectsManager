package ru.kifor4ik.ProjectsManager.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.kifor4ik.ProjectsManager.Configs.Security.Security;
import ru.kifor4ik.ProjectsManager.Entity.TaskEntity;
import ru.kifor4ik.ProjectsManager.Exeptions.ProjectNotFoundException;
import ru.kifor4ik.ProjectsManager.Exeptions.TaskNotExistException;
import ru.kifor4ik.ProjectsManager.Exeptions.YouAreNotAdminException;
import ru.kifor4ik.ProjectsManager.Models.NewTaskModel;
import ru.kifor4ik.ProjectsManager.Models.TaskModel;
import ru.kifor4ik.ProjectsManager.Service.ProjectService;
import ru.kifor4ik.ProjectsManager.Service.TaskService;
import ru.kifor4ik.ProjectsManager.Service.UserService;

@RestController
@RequestMapping("/project/task")
public class TaskController {

    @Autowired
    public TaskService taskService;

    @Autowired
    public ProjectService projectService;

    @Autowired
    public UserService userService;

    @PostMapping
    public ResponseEntity newTask(@RequestBody NewTaskModel model) {

        try {
            return ResponseEntity.ok().body(TaskModel.toModel(taskService.newTask(model,
                    "test@test")).getName() + " successfully created");

        } catch (ProjectNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{taskId}")
    public ResponseEntity getTask(@PathVariable Long taskId) {

        try {
            return ResponseEntity.ok().body(TaskModel.toModel(taskService.getTask(taskId)));
        } catch (TaskNotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity editTask(@RequestBody TaskEntity task) {

        try {
            return ResponseEntity.ok().body(TaskModel.toModel(taskService.editTask(task)));
        } catch (ProjectNotFoundException | TaskNotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/{taskId}")
    public ResponseEntity deleteTask(@PathVariable Long taskId) {

        try {
            return ResponseEntity.ok().body(taskService.deleteTask(taskId) + " deleted");
        } catch (TaskNotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity getAllTasks(@PageableDefault(sort = {"id"},direction = Sort.Direction.DESC, page = 0,size = 5,value = 5) Pageable pageable,
                                      @RequestParam(required = false) long size, @RequestParam(required = false) long page) {
        return ResponseEntity.ok().body(taskService.getAllTasks(pageable));
    }

}
