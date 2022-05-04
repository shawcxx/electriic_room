package com.shawcxx.modules.sys.dto;

import cn.hutool.core.bean.BeanUtil;
import com.shawcxx.common.validate.Mobile;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.shawcxx.modules.sys.domain.SysUserDO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author cjl
 * @create 2020/3/24
 */
@Data
@NoArgsConstructor
public class SysUserDTO {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;


    /**
     * 姓名
     */
    private String name;

    private Date createTime;

    private Date lastLoginTime;

    private Long deptId;
    private Integer userStatus;


    /**
     * 角色ID列表
     */
    private List<Long> roleIdList;

    public SysUserDTO(SysUserDO sysUserDO) {
        BeanUtil.copyProperties(sysUserDO, this);
    }
}
