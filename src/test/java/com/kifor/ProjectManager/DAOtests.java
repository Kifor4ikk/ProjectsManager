package com.kifor.ProjectManager;

import com.kifor.ProjectManager.Entities.Projects.Project;
import com.kifor.ProjectManager.Entities.Status;
import com.kifor.ProjectManager.Entities.Task.Task;
import com.kifor.ProjectManager.Models.TaskCreateModel;
import com.kifor.ProjectManager.Models.UserLoginModel;
import com.kifor.ProjectManager.Services.ProjectService;
import com.kifor.ProjectManager.Services.TaskService;
import com.kifor.ProjectManager.Services.UserService;
import io.swagger.models.Model;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLOutput;


@RunWith(MockitoJUnitRunner.class)
public class DAOtests {


    @Test
    public void transitFromLineToTask(){

        //task 1
        Task task2 = new Task();
        task2.setId(1);
        task2.setProjectId(1);
        task2.setName("head");
        task2.setBody("body");
        task2.setStatus(Status.ACTIVE);

        //task2
        Task task;
        String line = "1,1,head,body,0";
        task = Task.fromString(line);

        //field should be equals
        Assert.assertEquals(task,task2);
    }

}
