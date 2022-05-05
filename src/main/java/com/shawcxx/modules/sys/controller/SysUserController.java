package com.shawcxx.modules.sys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shawcxx.common.base.MyResult;
import com.shawcxx.modules.sys.dto.SysUserDTO;
import com.shawcxx.modules.sys.form.SysUserForm;
import com.shawcxx.modules.sys.form.SysUserQueryForm;
import com.shawcxx.modules.sys.service.SysUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author: dj
 * @create: 2020-03-04 08:58
 * @description:
 */
@RestController
@RequestMapping("sys/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @PostMapping("info")
    @SaCheckRole("admin")
    public MyResult info(@RequestParam Long id) {
        SysUserDTO sysUserDTO = sysUserService.info(id);
        return MyResult.data(sysUserDTO);
    }

    @PostMapping("saveOrUpdate")
    @SaCheckRole("admin")
    public MyResult saveOrUpdate(@RequestBody @Validated SysUserForm form) {
        sysUserService.saveOrUpdate(form);
        return MyResult.ok();
    }


    @PostMapping("query")
    @SaCheckRole("admin")
    public MyResult query(@RequestBody SysUserQueryForm form) {
        Page<SysUserDTO> page = sysUserService.query(form);
        return MyResult.data(page);
    }
}
