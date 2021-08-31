package com.kifor.ProjectManager.Controllers;

import com.kifor.ProjectManager.Entities.Task.Task;
import com.kifor.ProjectManager.Models.TaskCreateModel;
import com.kifor.ProjectManager.Services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/projects/tasks")
public class TaskController {

    @Autowired
    public TaskService taskService;

    @PostMapping
    public String newTask(@RequestBody TaskCreateModel model){
        return taskService.newTask(model);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping
    public String edit(@RequestBody TaskCreateModel model, @RequestParam long id){
        return taskService.editTask(model,id);
    }

    @GetMapping("/{id}")
    public Task getById(@PathVariable long id){
        return taskService.getTask(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping
    public String delete(@RequestParam long id){
        return taskService.deleteTask(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/all")
    public Page<Task> getAll(@RequestParam(defaultValue = "0",required = false) int page,
                             @RequestParam(defaultValue = "5",required = false) int size){
        return taskService.getAll(page,size);
    }
}
