package ru.kifor4ik.ProjectsManager.Exeptions;

public class TaskNotExistException extends Exception{

    public TaskNotExistException(String message) {
        super(message);
    }
}
