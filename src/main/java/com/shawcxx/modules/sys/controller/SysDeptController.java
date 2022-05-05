package com.shawcxx.modules.sys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shawcxx.common.base.MyResult;
import com.shawcxx.modules.sys.dto.SysDeptDTO;
import com.shawcxx.modules.sys.form.SysDeptForm;
import com.shawcxx.modules.sys.form.SysDeptQueryForm;
import com.shawcxx.modules.sys.service.SysDeptService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author cjl
 * @date 2022/5/5 9:27
 * @description
 */
@RestController
@RequestMapping("sys/dept")
public class SysDeptController {

    @Resource
    private SysDeptService sysDeptService;

    @PostMapping("query")
    @SaCheckLogin
    public MyResult list(@RequestBody SysDeptQueryForm form) {
        Page<SysDeptDTO> page = sysDeptService.queryPage(form);
        return MyResult.data(page);
    }

    @PostMapping("del")
    @SaCheckRole("admin")
    public MyResult del(@RequestParam Long id) {
        sysDeptService.del(id);
        return MyResult.ok();
    }

    @PostMapping("saveOrUpdate")
    @SaCheckRole("admin")
    public MyResult saveOrUpdate(@RequestBody @Validated SysDeptForm form) {
        sysDeptService.saveOrUpdate(form);
        return MyResult.ok();
    }
}
