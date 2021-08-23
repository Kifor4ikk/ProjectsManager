package ru.kifor4ik.ProjectsManager.Exeptions;

public class YouAreNotAdminException extends Exception{
    public YouAreNotAdminException(String message) {
        super(message);
    }
}
