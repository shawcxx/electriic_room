package com.shawcxx.modules.sys.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author: dj
 * @create: 2020-03-04 09:02
 * @description: 登录表单
 */
@Data
public class LoginForm implements Serializable {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "验证码不能为空")
    private String verifyCode;

    @NotBlank(message = "验证码不能为空")
    private String verifyCodeToken;

}
