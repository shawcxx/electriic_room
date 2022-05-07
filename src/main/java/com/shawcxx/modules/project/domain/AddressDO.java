package com.shawcxx.modules.project.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * @author cjl
 * @date 2022/5/7 10:44
 * @description
 */
@Data
@TableName("t_address")
public class AddressDO {
    @TableId(type = IdType.ASSIGN_UUID)
    private String addressId;
    private Integer addressType;
    private String addressName;
    private String projectId;
    private String parentAddressId;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date createTime;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date updateTime;
}
