package com.shawcxx.modules.project.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.shawcxx.common.base.MyResult;
import com.shawcxx.modules.project.form.AddressForm;
import com.shawcxx.modules.project.service.AddressService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author cjl
 * @date 2022/5/7 10:47
 * @description
 */
@RestController
@RequestMapping("address")
public class AddressController {
    @Resource
    private AddressService addressService;

    @PostMapping("add")
    @SaCheckLogin
    public MyResult addAddress(@RequestBody @Validated AddressForm form) {
        return MyResult.ok();
    }
}
