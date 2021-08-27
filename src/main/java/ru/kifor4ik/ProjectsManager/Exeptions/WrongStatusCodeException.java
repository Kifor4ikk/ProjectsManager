package ru.kifor4ik.ProjectsManager.Exeptions;

public class WrongStatusCodeException extends Exception{

    public WrongStatusCodeException(String message) {
        super(message);
    }
}
