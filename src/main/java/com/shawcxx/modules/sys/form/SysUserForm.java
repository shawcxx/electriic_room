package com.shawcxx.modules.sys.form;

import com.shawcxx.common.validate.Mobile;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author cjl
 * @create 2020/3/11
 */
@Data
public class SysUserForm {
    private Long userId;

    @Mobile
    private String username;

    /**
     * 密码
     */
    @Size(max = 100, message = "密码太长")
    private String password;

    private String name;

    /**
     * 角色ID列表
     */
    private List<Long> roleIdList;

    private Integer userStatus;
}
