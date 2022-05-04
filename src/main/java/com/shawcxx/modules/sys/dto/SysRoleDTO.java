package com.shawcxx.modules.sys.dto;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.shawcxx.modules.sys.domain.SysRoleDO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * @author cjl
 * @create 2020.4.8
 */
@Data
public class SysRoleDTO {
    private Long roleId;

    private String roleName;
    private String roleCode;
    private String remark;

    private Date createTime;

    private List<Long> menuIdList;


    public SysRoleDTO(SysRoleDO sysRoleDO) {
        BeanUtil.copyProperties(sysRoleDO, this);
    }

}
