package com.shawcxx.modules.project.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.shawcxx.common.base.MyResult;
import com.shawcxx.modules.project.form.AddressForm;
import com.shawcxx.modules.project.service.AddressService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public MyResult add(@RequestBody @Validated AddressForm form) {
        addressService.add(form);
        return MyResult.ok();
    }

    @PostMapping("delete")
    @SaCheckLogin
    public MyResult delete(@RequestParam String id) {
        addressService.delete(id);
        return MyResult.ok();
    }
}
