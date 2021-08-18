package ru.kifor4ik.ProjectsManager.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kifor4ik.ProjectsManager.Entity.ProjectEntity;
import ru.kifor4ik.ProjectsManager.Entity.TaskEntity;
import ru.kifor4ik.ProjectsManager.Exeptions.ProjectNotFoundException;
import ru.kifor4ik.ProjectsManager.Exeptions.TaskNotExistException;
import ru.kifor4ik.ProjectsManager.Models.TaskModel;
import ru.kifor4ik.ProjectsManager.Repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    public TaskRepository taskRepository;
    @Autowired
    public ProjectService projectService;

    public TaskEntity newTask(Long projectId, TaskModel task) throws ProjectNotFoundException {

        ProjectEntity projectEntity = projectService.findById(projectId);

        if(projectEntity != null) {
            TaskEntity taskEntity = TaskEntity.fromModel(projectEntity, task);
            return taskRepository.save(taskEntity);
        } else{
            throw new ProjectNotFoundException("Cant bound task with project.. Project not found");
        }
    }

    public TaskEntity getTask(Long id) throws TaskNotExistException {

        TaskEntity task = taskRepository.findById(id).get();

        if(taskRepository.findById(id) != null){
            return task;
        } else {
            throw new TaskNotExistException("Task with current id not exist");
        }
    }

    public TaskEntity editTask(TaskEntity task) throws ProjectNotFoundException {

        if (projectService.findById(task.getProjectId()) == null)
            throw new ProjectNotFoundException("Cant bound task with project.. Project not found");

        if (taskRepository.findById(task.getId()) == null) throw new ProjectNotFoundException("Task not found...");

        return taskRepository.save(task);
    }

    public Long deleteTask(Long id) throws TaskNotExistException {

        taskRepository.delete(getTask(id));
        return id;
    }

    public List<TaskModel> getAllTasks(){

        List<TaskModel> taskModelsList = new ArrayList<>();

        for(TaskEntity entity : taskRepository.findAll()){
            taskModelsList.add(TaskModel.toModel(entity));
        }

        return taskModelsList;
    }
}
