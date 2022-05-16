package com.shawcxx.modules.project.bo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * @author Chen jl
 * @date 2022/5/15 13:34
 * @description
 **/
@Data
@NoArgsConstructor
public class ProjectImportBO {
    private String name;
    private Integer type;
    //1 项目 2 地址 3 设备
    private Integer flag;
    private String imei;
    private String modbus;
    @JSONField(ordinal = 1)
    private List<ProjectImportBO> child;

    public ProjectImportBO(String name) {
        this.name = name;
        this.flag = 1;
        this.child = new ArrayList<>();
    }

    public void addChildDevice(String name, Integer type, String imei, String modbus) {
        long count = this.getChild().stream().filter(projectImportBO -> projectImportBO.getName().equals(name)).count();
        if (count == 0) {
            ProjectImportBO projectImportBO = new ProjectImportBO();
            projectImportBO.setName(name);
            projectImportBO.setType(type);
            projectImportBO.setFlag(3);
            projectImportBO.setImei(imei);
            projectImportBO.setModbus(modbus);
            this.child.add(projectImportBO);
        }
    }

    public void addChildAddress(String name, Integer type, Boolean haveChild) {
        long count = this.getChild().stream().filter(projectImportBO -> projectImportBO.getName().equals(name)).count();
        if (count == 0) {
            ProjectImportBO projectImportBO = new ProjectImportBO();
            projectImportBO.setName(name);
            projectImportBO.setType(type);
            projectImportBO.setFlag(2);
            if (haveChild) {
                projectImportBO.setChild(new ArrayList<>());
            }
            this.child.add(projectImportBO);
        }
    }

    public ProjectImportBO getChildByName(String name) {
        for (ProjectImportBO projectImportBO : this.child) {
            if (projectImportBO.getName().equals(name)) {
                return projectImportBO;
            }
        }
        return null;
    }
}
