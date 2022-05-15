package com.shawcxx.modules.project.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Chen jl
 * @date 2022/5/15 13:34
 * @description
 **/
@Data
public class ProjectImportBO {
    private String name;
    private Integer type;
    //1 项目 2 地址 3 设备
    private Integer flag;
    private String imei;
    private String addr;
    private Set<ProjectImportBO> child;

    public ProjectImportBO(String name) {
        this.name = name;
        this.child = new HashSet<>();
    }
}
