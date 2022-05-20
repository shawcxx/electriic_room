package com.shawcxx.modules.project.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author cjl
 * @date 2022/5/13 16:54
 * @description
 */
@Data
public class AddressForm {
    private String addressId;
    @NotNull(message = "房间类型不能为空")
    private Integer addressType;
    @NotBlank(message = "房间名称不能为空")
    private String addressName;
    @NotBlank(message = "参数错误")
    private String parentAddressId;
}
