package com.shawcxx.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: dj
 * @create: 2020-10-29 09:43
 * @description: 角色
 */
@Data
@TableName("sys_role")
public class SysRoleDO implements Serializable {

    /**
     * 角色ID
     */
    @TableId(type = IdType.AUTO)
    private Long roleId;


    private String roleCode;
    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 备注
     */
    private String remark;


    /**
     * 创建时间
     */
    private Date createTime;


}
