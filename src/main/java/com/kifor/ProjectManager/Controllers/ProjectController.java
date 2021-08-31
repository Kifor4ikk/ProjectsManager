package com.kifor.ProjectManager.Controllers;

import com.kifor.ProjectManager.Entities.Projects.Project;
import com.kifor.ProjectManager.Models.ProjectCreateModel;
import com.kifor.ProjectManager.Services.ProjectService;
import com.kifor.ProjectManager.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @PostMapping
    public String newProject(@RequestBody ProjectCreateModel model){
        return projectService.newProject(model);
    }

    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Long id){
        return projectService.findById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping
    public String editProject(@RequestBody ProjectCreateModel model){
        return projectService.edit(model);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping
    public String deleteProject(@RequestParam long id){
        return projectService.delete(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/all")
    public Page<Project> getAllProjects(@RequestParam(defaultValue = "0",required = false) int page,
                                        @RequestParam(defaultValue = "5",required = false) int size){
        return projectService.getAll(page, size);
    }

}
