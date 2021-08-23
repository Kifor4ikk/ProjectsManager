package ru.kifor4ik.ProjectsManager.Service;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.kifor4ik.ProjectsManager.Entity.ProjectEntity;
import ru.kifor4ik.ProjectsManager.Entity.UserEntity;
import ru.kifor4ik.ProjectsManager.Exeptions.ProjectAlreadyExistException;
import ru.kifor4ik.ProjectsManager.Exeptions.ProjectNotFoundException;
import ru.kifor4ik.ProjectsManager.Exeptions.UserNotFoundExeption;
import ru.kifor4ik.ProjectsManager.Models.ProjectModel;
import ru.kifor4ik.ProjectsManager.Repository.ProjectRepository;
import ru.kifor4ik.ProjectsManager.Repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProjectService {

    @Autowired
    public ProjectRepository projectRepository;

    @Autowired
    public UserRepository userRepository;

    /**
     *
     * @param projectModel include project NAME and ADMIN ID
     *                     Admin is first project user and who can create, update, read, delete tasks
     *                     of this project.
     *                     And ALL projects have personaly access code which genereted here.
     *                     user can join to project by using special code
     * @return New project with ID and first user
     * @throws ProjectAlreadyExistException if project with current name already exist
     * @throws UserNotFoundExeption if trying create project with admin who not exist
     */
    public ProjectEntity newProject(ProjectModel projectModel) throws ProjectAlreadyExistException, UserNotFoundExeption {

        if(projectRepository.findByName(projectModel.getName()) != null) throw new ProjectAlreadyExistException("Project with this name already exist");

        UserEntity user = userRepository.findById(projectModel.getAdminId()).get();
        if(user == null) throw new UserNotFoundExeption("User who should be admin not found");

        ProjectEntity project = ProjectEntity.fromModel(projectModel);

        do{
            project.setAccessCode(ProjectEntity.generateCode());
        } while(projectRepository.findByAccessCode(project.getAccessCode()) != null);
        projectRepository.save(project);

        user.getProjects().add(project);
        userRepository.save(user);
        projectRepository.save(project);

        return project;
    }

    /**
     *
     * @param id project id, no more no less
     * @return project if project was found
     * @throws ProjectNotFoundException - If project not found :|
     */
    public ProjectEntity findById(Long id) throws ProjectNotFoundException {

        try {
            return projectRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new ProjectNotFoundException("Project not found");
        }
    }

    /**
     *  If be honest this func must call RENAME)
     *  cause u can rename project and give admin rights to another persone
     * @param id Project ID
     * @param projectNew new Project params
     * @return edited project
     * @throws ProjectNotFoundException - project not found
     * @throws ProjectAlreadyExistException - project with NEW name already exist
     */
    public ProjectEntity editProject(Long id, ProjectModel projectNew) throws ProjectNotFoundException, ProjectAlreadyExistException {

        ProjectEntity project = this.findById(id);
        if(projectRepository.findByName(projectNew.getName()) != null){
            throw new ProjectAlreadyExistException("Project with current name already exist!");
        }
        project.setAdminId(projectNew.getAdminId());
        project.setName(projectNew.getName());

        return projectRepository.save(project);
    }

    /**
     *
     * @param id project ID which should be deleted
     * @return deleted project
     * @throws ProjectNotFoundException - project not found
     */
    public ProjectEntity deleteProject(Long id) throws ProjectNotFoundException {
        ProjectEntity project = findById(id);
        projectRepository.delete(project);
        return project;
    }

    /**
     * Get all projects, func for profiles with admin authority
     * @return List of all projects
     */
    public List<ProjectModel> getAll(){

        List<ProjectModel> projectModelList = new ArrayList<>();
        for(ProjectEntity task : projectRepository.findAll()){
            projectModelList.add(ProjectModel.toModel(task));
        }
        return projectModelList;
    }
}
