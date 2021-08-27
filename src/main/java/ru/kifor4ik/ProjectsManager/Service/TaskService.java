package ru.kifor4ik.ProjectsManager.Service;


import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.kifor4ik.ProjectsManager.Entity.ProjectEntity;
import ru.kifor4ik.ProjectsManager.Entity.Roles;
import ru.kifor4ik.ProjectsManager.Entity.Status;
import ru.kifor4ik.ProjectsManager.Entity.TaskEntity;
import ru.kifor4ik.ProjectsManager.Exeptions.ProjectNotFoundException;
import ru.kifor4ik.ProjectsManager.Exeptions.TaskNotExistException;
import ru.kifor4ik.ProjectsManager.Exeptions.UserNotFoundExeption;
import ru.kifor4ik.ProjectsManager.Exeptions.YouAreNotAdminException;
import ru.kifor4ik.ProjectsManager.Models.NewTaskModel;
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
    @Autowired
    public UserService userService;

    /**
     * Func for new task create
     * @param email user email to check is user has admin access to this project
     * @param task new Task model without ID
     * @return new Task
     * @throws ProjectNotFoundException project which must take care not exist
     */
    public TaskEntity newTask(NewTaskModel task, String email) throws ProjectNotFoundException, UserNotFoundExeption, YouAreNotAdminException {

        ProjectEntity projectEntity = projectService.findById(task.getProjectId());
        if(projectEntity != null) {

            if(projectEntity.getAdminId() != userService.findByEmail(email).getId() && !userService.findByEmail(email).getRole().equals(Roles.ADMIN))
                throw new YouAreNotAdminException("You are not admin of this project");

            TaskEntity taskEntity =  NewTaskModel.toEntity(task);
            taskEntity.setProject(projectEntity);
            taskEntity.setTaskStatus(Status.ACTIVE);
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

        if (projectService.findById(task.getProjectId()) == null )
            throw new ProjectNotFoundException("Cant bound task with project.. Project not found");

        if (taskRepository.findById(task.getId()) == null || task.getTaskStatus().equals(Status.DELETED))
            throw new TaskNotExistException("Task not found...");

        return taskRepository.save(task);
    }

    /**
     *
     * @param id id of Task whick should be deleted
     * @return
     * @throws TaskNotExistException
     */
    public Long deleteTask(Long id) throws TaskNotExistException {

        TaskEntity task = getTask(id);
        task.setTaskStatus(Status.DELETED);
        taskRepository.save(task);
        return id;
    }

    /**
     * ALL TASKS FROM ALL PROJECTS!
     * INFINITY POWER!
     * @return list of all tasks
     */
    public List<TaskModel> getAllTasks(Pageable pageable){

        List<TaskModel> taskModelsList = new ArrayList<>();

        for(TaskEntity entity : taskRepository.findAll(pageable)){
            taskModelsList.add(TaskModel.toModel(entity));
        }

        return taskModelsList;
    }
}
