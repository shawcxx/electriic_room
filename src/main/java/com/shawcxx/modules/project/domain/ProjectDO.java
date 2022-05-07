package com.shawcxx.modules.project.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * @author cjl
 * @date 2022/5/7 9:54
 * @description
 */
@Data
@TableName("t_project")
public class ProjectDO {
    @TableId(type = IdType.ASSIGN_UUID)
    private String projectId;

    private String projectName;

    private Double latitude;
    private Double longitude;
    private Long deptId;
    private String address;

    private String manager;
    private String phone;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date createTime;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date updateTime;

}
