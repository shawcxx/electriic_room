package com.shawcxx.modules.sys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shawcxx.common.annotation.SysLog;
import com.shawcxx.common.base.MyResult;
import com.shawcxx.modules.sys.domain.SysRoleDO;
import com.shawcxx.modules.sys.dto.SysRoleDTO;
import com.shawcxx.modules.sys.form.SysRoleForm;
import com.shawcxx.modules.sys.form.SysRoleQueryForm;
import com.shawcxx.modules.sys.service.SysRoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author cjl
 * @create 2020.4.8
 */
@RestController
@RequestMapping("sys/role")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;


    @PostMapping("query")
    @SaCheckRole("admin")
    public MyResult list(@RequestBody SysRoleQueryForm form) {
        Page<SysRoleDO> page = sysRoleService.query(form);
        return MyResult.data(page);
    }

    @GetMapping("del")
    @SysLog("删除角色")
    @SaCheckRole("admin")
    public MyResult deleteById(@RequestParam Long id) {
        sysRoleService.delete(id);
        return MyResult.ok("删除成功");

    }

    @PostMapping("saveOrUpdate")
    @SysLog("新增或修改角色")
    @SaCheckRole("admin")
    public MyResult insertOrUpdate(@RequestBody @Validated SysRoleForm form) {
        sysRoleService.saveOrUpdate(form);
        return MyResult.ok();

    }

    @PostMapping("info")
    @SaCheckRole("admin")
    public MyResult queryPage(@RequestParam Long id) {
        SysRoleDTO sysRoleDTO = sysRoleService.info(id);
        return MyResult.data(sysRoleDTO);
    }
}
