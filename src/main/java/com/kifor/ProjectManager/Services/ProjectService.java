package com.kifor.ProjectManager.Services;


import com.kifor.ProjectManager.DAO.ProjectRepository;
import com.kifor.ProjectManager.DAO.TaskRepository;
import com.kifor.ProjectManager.DAO.UserRepository;
import com.kifor.ProjectManager.Entities.Projects.Project;
import com.kifor.ProjectManager.Entities.Status;
import com.kifor.ProjectManager.Entities.Task.Task;
import com.kifor.ProjectManager.Entities.User.User;
import com.kifor.ProjectManager.Models.ProjectCreateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    public String newProject(ProjectCreateModel model){
        if(projectRepository.findByName(model.getName()) != null)
            return "Project with current name already exist";
        if(userRepository.findById(model.getAdminId()) == null)
            return "User not found";

        Project project = new Project();
        project.setAdminId(model.getAdminId());
        project.setName(model.getName());
        project.setStatus(Status.ACTIVE);
        do{
            project.setAccessCode(generateCode(10));
        } while (projectRepository.findByAccessCode(project.getAccessCode()) != null);
        projectRepository.save(project);
        projectRepository.addUserToProject(project.getAdminId(), projectRepository.findByAccessCode(project.getAccessCode()).getId());
        return project.getName() + " was created!";
    }

    public String edit(ProjectCreateModel model){
        Project project = projectRepository.findByName(model.getName());

        if(project == null) return "Project not found";

        project.setName(model.getName());
        projectRepository.save(project);
        return project.getId() + "# was changed!";
    }

    public String delete(long id){
        Project project = projectRepository.findById(id);
        if(project == null) return "Project not found";
        project.setStatus(Status.DELETED);

        Task task;
        for(String line :  projectRepository.findAllProjectTasks(id)){
            task = Task.fromString(line);
            task.setStatus(Status.DELETED);
            taskRepository.save(task);
        }
        return project.getName() + " was deleted!";
    }

    public Project findById(long id){
        Project project = projectRepository.findById(id);
        if(project == null) return null;
        project.setUserList(projectRepository.findAllProjectUsers(id));

        Set<Task> taskList = new HashSet<>();

        for(String line : projectRepository.findAllProjectTasks(id)){
            taskList.add(Task.fromString(line));
        }
        project.setTaskList(taskList);
        return project;
    }

    public Page<Project> getAll(int page, int size) {
        final Pageable pageable = PageRequest.of(page, size);

        List<Project> list = projectRepository.findAll(pageable).stream()
                .collect(Collectors.toList());

        for (Project project : list) {
            project = findById(project.getId());
        }

        return new PageImpl<>(list, pageable, list.size());
    }

    public static String generateCode(int size){
        String code = "";
        boolean valid = false;
        Random rand = new Random();
        for(int i = 0; i < size; i++){
            valid = false;
            while(valid != true) {
                switch (rand.nextInt(3)) {
                    case 0: {
                        code += (char) (rand.nextInt(10) + 48);
                        valid = true;
                        break;
                    }
                    case 1: {
                        code += (char) (rand.nextInt(26) + 65);
                        valid = true;
                        break;
                    }
                    case 2: {
                        code += (char) (rand.nextInt(26) + 97);
                        valid = true;
                        break;
                    }
                }
            }
        }
        return code;
    }
}
