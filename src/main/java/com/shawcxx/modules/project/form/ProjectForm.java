package com.shawcxx.modules.project.form;

import com.shawcxx.common.validate.Mobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Chen jl
 * @date 2022/5/30 20:24
 * @description
 **/
@Data
public class ProjectForm {
    @NotBlank(message = "参数错误")
    private String projectId;

    @Size(max = 100, message = "项目名称长度不能超过100字符")
    private String projectName;

    private Double latitude;
    private Double longitude;
    @Size(max = 100, message = "项目地址长度不能超过100字符")
    private String address;

    @Size(max = 10, message = "联系人长度不能超过10字符")
    private String manager;
    @Mobile
    private String phone;
}
