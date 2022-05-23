package com.shawcxx.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author cjl
 * @date 2022/5/5 9:28
 * @description
 */
@Data
@TableName("sys_dept")
public class SysDeptDO {
    @TableId(type = IdType.AUTO)
    private Long deptId;

    private String deptName;

    private String manager;
    private String phone;
    private String address;
}
