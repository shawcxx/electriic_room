package com.shawcxx.modules.project.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cjl
 * @date 2022/5/7 10:02
 * @description
 */
@Data
public class ProjectListDTO {
    private String deptName;
    private List<ProjectDTO> projectList;


    public ProjectListDTO() {
        this.projectList = new ArrayList<>();
    }
}
