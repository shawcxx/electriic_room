package com.shawcxx.modules.sys.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.shawcxx.common.base.MyResult;
import com.shawcxx.modules.sys.form.LoginForm;
import com.shawcxx.modules.sys.service.SysLoginService;
import com.shawcxx.modules.sys.service.SysVerificationCodeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author: dj
 * @create: 2020-03-04 08:58
 * @description:
 */
@RestController
@RequestMapping("sys")
public class SysLoginController {

    @Resource
    private SysLoginService sysLoginService;

    @Resource
    private SysVerificationCodeService sysVerificationCodeService;

    @PostMapping("login")
    public MyResult login(@RequestBody @Validated LoginForm form) {
        sysVerificationCodeService.check(form.getVerifyCode(), form.getVerifyCodeToken());
        return sysLoginService.login(form);
    }

    @PostMapping("getVerificationCode")
    public MyResult getPicVerificationCode() {
        return sysVerificationCodeService.getVerificationCode(null);
    }


    @GetMapping("logout")
    @SaCheckLogin
    public MyResult logout() {
        return sysLoginService.logout();
    }

}
