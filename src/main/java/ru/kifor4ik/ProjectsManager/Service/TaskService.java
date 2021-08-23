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

    /**
     * Func for new task create
     * @param projectId project that takes care about this task)
     * @param task new Task model without ID
     * @return new Task
     * @throws ProjectNotFoundException project which must take care not exist
     */
    public TaskEntity newTask(Long projectId, TaskModel task) throws ProjectNotFoundException {

        ProjectEntity projectEntity = projectService.findById(projectId);

        if(projectEntity != null) {
            TaskEntity taskEntity = TaskEntity.fromModel(projectEntity, task);
            return taskRepository.save(taskEntity);
        } else{
            throw new ProjectNotFoundException("Cant bound task with project.. Project not found");
        }
    }

    /**
     *
     * @param id TASK ID
     * @return task with current id
     * @throws TaskNotExistException - Task not exist
     */
    public TaskEntity getTask(Long id) throws TaskNotExistException {

        TaskEntity task = taskRepository.findById(id).get();

        if(taskRepository.findById(id) != null){
            return task;
        } else {
            throw new TaskNotExistException("Task with current id not exist");
        }
    }

    /**
     *
     * @param task new task NAME and BODY
     * @return edited task
     * @throws ProjectNotFoundException - project not exist(this cant happend, i hope)
     * @throws TaskNotExistException - task not exist
     */
    public TaskEntity editTask(TaskEntity task) throws ProjectNotFoundException, TaskNotExistException {

        if (projectService.findById(task.getProjectId()) == null)
            throw new ProjectNotFoundException("Cant bound task with project.. Project not found");

        if (taskRepository.findById(task.getId()) == null) throw new TaskNotExistException("Task not found...");

        return taskRepository.save(task);
    }

    /**
     *
     * @param id id of Task whick should be deleted
     * @return
     * @throws TaskNotExistException
     */
    public Long deleteTask(Long id) throws TaskNotExistException {

        taskRepository.delete(getTask(id));
        return id;
    }

    /**
     * ALL TASKS FROM ALL PROJECTS!
     * INFINITY POWER!
     * @return list of all tasks
     */
    public List<TaskModel> getAllTasks(){

        List<TaskModel> taskModelsList = new ArrayList<>();

        for(TaskEntity entity : taskRepository.findAll()){
            taskModelsList.add(TaskModel.toModel(entity));
        }

        return taskModelsList;
    }
}
