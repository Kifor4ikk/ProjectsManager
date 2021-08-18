package ru.kifor4ik.ProjectsManager.Exeptions;

public class ProjectAlreadyExistException extends Exception{

    public ProjectAlreadyExistException(String message) {
        super(message);
    }
}
