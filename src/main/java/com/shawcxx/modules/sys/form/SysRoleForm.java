package com.shawcxx.modules.sys.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author cjl
 * @create 2020.4.8
 */
@Data
public class SysRoleForm {

    private Long roleId;

    @Size(max = 50, message = "角色名称不能超过50位")
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    @Size(max = 50, message = "角色代号不能超过50位")
    @NotBlank(message = "角色代号不能为空")
    private String roleCode;

    @Size(max = 100, message = "描述不能超过100位")
    private String remark;

    private List<Long> menuIdList;

}
