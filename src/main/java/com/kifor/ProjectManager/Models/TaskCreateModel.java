package com.kifor.ProjectManager.Models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class TaskCreateModel {

    private String name;
    private String body;
    private Long projectId;
}
