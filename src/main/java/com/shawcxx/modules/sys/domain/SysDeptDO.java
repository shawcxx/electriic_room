package com.shawcxx.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author cjl
 * @date 2022/5/5 9:28
 * @description
 */
@Data
public class SysDeptDO {
    @TableId(type = IdType.AUTO)
    private Long deptId;

    private String deptName;

}
