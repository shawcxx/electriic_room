package com.shawcxx.modules.sys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.shawcxx.common.annotation.SysLog;
import com.shawcxx.common.base.MyResult;
import com.shawcxx.modules.sys.dto.SysMenuDTO;
import com.shawcxx.modules.sys.form.SysMenuForm;
import com.shawcxx.modules.sys.service.SysMenuService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author cjl
 * @create 2020.4.3
 */
@RestController
@RequestMapping("sys/menu")
public class SysMenuController {
    @Resource
    private SysMenuService sysMenuService;


    @PostMapping("list")
    @SaCheckLogin
    @SaCheckRole("admin")
    public MyResult list() {
        List<SysMenuDTO> list = sysMenuService.sysMenuList(true);
        return MyResult.data(list);
    }

    @PostMapping("getMenuSelectTree")
    @SaCheckLogin
    @SaCheckRole("admin")
    public MyResult getMenuSelectTree() {

        SysMenuDTO dto = sysMenuService.getMenuSelectTree();
        return MyResult.data(dto);

    }

    @SysLog("删除菜单")
    @PostMapping("del")
    @SaCheckLogin
    @SaCheckRole("admin")
    public MyResult deleteById(@RequestParam Long id) {
        sysMenuService.delete(id);
        return MyResult.ok();
    }

    @SysLog("编辑菜单")
    @PostMapping("saveOrUpdate")
    @SaCheckLogin
    @SaCheckRole("admin")
    public MyResult addOrUpdate(@RequestBody @Validated SysMenuForm form) {
        return sysMenuService.saveOrUpdate(form);
    }

}
