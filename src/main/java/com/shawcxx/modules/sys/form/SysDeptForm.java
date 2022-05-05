package com.shawcxx.modules.sys.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author cjl
 * @date 2022/5/5 9:35
 * @description
 */
@Data
public class SysDeptForm {
    private Long deptId;
    @NotBlank(message = "运营商名称不能为空")
    @Size(max = 50,message = "运营商名称过长")
    private String deptName;
}
