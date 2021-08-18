package ru.kifor4ik.ProjectsManager.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kifor4ik.ProjectsManager.Entity.TaskEntity;
import ru.kifor4ik.ProjectsManager.Exeptions.ProjectNotFoundException;
import ru.kifor4ik.ProjectsManager.Exeptions.TaskNotExistException;
import ru.kifor4ik.ProjectsManager.Models.TaskModel;
import ru.kifor4ik.ProjectsManager.Service.TaskService;

@RestController
@RequestMapping("/project/task")
public class TaskController {

    @Autowired
    public TaskService taskService;


    @PostMapping
    public ResponseEntity newTask(@RequestParam Long projectId, @RequestBody TaskModel task) {

        try {
            return ResponseEntity.ok().body(TaskModel.toModel(taskService.newTask(projectId, task)).getName() + " successfully created");

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
        } catch (ProjectNotFoundException e) {
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
    public ResponseEntity getAllTasks() {
        return ResponseEntity.ok().body(taskService.getAllTasks());
    }

}
