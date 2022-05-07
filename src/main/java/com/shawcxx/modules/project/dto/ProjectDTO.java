package com.shawcxx.modules.project.dto;

import cn.hutool.core.bean.BeanUtil;
import com.shawcxx.modules.project.domain.ProjectDO;
import lombok.Data;

import java.util.Date;

/**
 * @author cjl
 * @date 2022/5/7 10:02
 * @description
 */
@Data
public class ProjectDTO {
    private String projectId;
    private String projectName;
    private Double latitude;
    private Double longitude;
    private Long deptId;
    private String address;
    private Date createTime;
    private Date updateTime;
    private String manager;
    private String phone;
    private Long deviceNum;

    public ProjectDTO(ProjectDO projectDO) {
        BeanUtil.copyProperties(projectDO, this);
    }
}
