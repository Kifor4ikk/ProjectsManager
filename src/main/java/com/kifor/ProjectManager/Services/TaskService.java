package com.kifor.ProjectManager.Services;

import com.kifor.ProjectManager.DAO.ProjectRepository;
import com.kifor.ProjectManager.DAO.TaskRepository;
import com.kifor.ProjectManager.Entities.Status;
import com.kifor.ProjectManager.Entities.Task.Task;
import com.kifor.ProjectManager.Entities.User.User;
import com.kifor.ProjectManager.Models.TaskCreateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectService projectService;

    /**
     *
     * @param model, model of Task to create new or edit
     * @return Ok msg if created or msg about problem
     */
    public String newTask(TaskCreateModel model){
        if(projectService.findById(model.getProjectId()) == null) return "Project not found";

        Task task = new Task();
        task.setName(model.getName());
        task.setBody(model.getBody());
        task.setProjectId(model.getProjectId());
        task.setStatus(Status.ACTIVE);
        try {
            taskRepository.save(task);
        } catch (Exception e){
            e.printStackTrace();
        }
        return model.getName() + " was created ";
    }

    /**
     *
     * @param model, model of Task to create new or edit
     * @param taskId, id of task which should be edited
     * @return positive msg if all ok or bad msg if something wrong
     */
    public String editTask(TaskCreateModel model,long taskId){
        Task task = taskRepository.findById(taskId);
        if(task == null)
            return "Task not found";

        task.setName(model.getName());
        task.setBody(model.getBody());
        taskRepository.save(task);
        return taskId + " was changed!";
    }

    /**
     *
     * @param taskId, id of task which should be founded
     * @return
     */
    public Task getTask(long taskId){
        return taskRepository.findById(taskId);
    }

    /**
     *
     * @param taskId, id of delete task
     * @return positive msg if all ok, bad msg if not found
     */
    public String deleteTask(long taskId){
        Task task = taskRepository.findById(taskId);
        if(task == null)
            return "Task not found";

        task.setStatus(Status.DELETED);
        taskRepository.save(task);

        return taskId + " was deleted";
    }

    /**
     *
     * @param page number of page
     * @param size size of page
     * @return PageImp with Tasks
     */
    public Page<Task> getAll(int page, int size){

        final Pageable pageable = PageRequest.of(page, size);
        List<Task> list = taskRepository.findAll(pageable).stream()
                .collect(Collectors.toList());

        return new PageImpl<>(list, pageable, list.size());
    }
}
