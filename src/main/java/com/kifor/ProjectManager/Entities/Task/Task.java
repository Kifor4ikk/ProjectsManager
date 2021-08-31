package com.kifor.ProjectManager.Entities.Task;

import com.kifor.ProjectManager.Entities.Projects.Project;
import com.kifor.ProjectManager.Entities.Status;
import io.swagger.models.auth.In;
import lombok.*;
import org.springframework.jdbc.core.metadata.HsqlTableMetaDataProvider;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "task_entity", schema = "project_manager")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotNull
    @Column(name = "projectid")
    private long projectId;

    @NotEmpty(message = "Name should not be empty")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "Body should not be empty")
    @Column(name = "body")
    private String body;

    @NotNull
    @Column(name = "status")
    private Status status;

    public static Task fromString(String input){
        Task task = new Task();
        String[] line = input.split(",");
        task.id = Long.parseLong(line[0]);
        task.projectId = Long.parseLong(line[1]);
        task.name = line[2];
        task.body = line[3];
        switch (Integer.parseInt(line[4])){
            case 0 :{
                task.status = Status.ACTIVE;
                break;
            }
            case 1:{
                task.status = Status.DELETED;
                break;
            }
            case 2:{
                task.status = Status.BANNED;
                break;
            }
        }
        return task;
    }
}
