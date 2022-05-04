package com.shawcxx.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("sys_user")
public class SysUserDO implements Serializable {

    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    private String name;

    /**
     * 密码
     */
    private String password;


    private Date createTime;

    private Date lastLoginTime;

    private Long deptId;

    private Integer userStatus;
}
