package com.shawcxx.modules.sys.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.shawcxx.common.base.MyResult;
import com.shawcxx.common.base.SysUserBO;
import com.shawcxx.common.exception.MyException;
import com.shawcxx.modules.sys.domain.SysRoleDO;
import com.shawcxx.modules.sys.domain.SysUserDO;
import com.shawcxx.modules.sys.dto.SysMenuDTO;
import com.shawcxx.modules.sys.dto.SysUserLoginDTO;
import com.shawcxx.modules.sys.form.LoginForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: dj
 * @create: 2020-03-04 09:37
 * @description: 登录类
 */
@Service
@Slf4j
public class SysLoginService {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysMenuService sysMenuService;

    public MyResult login(LoginForm form) {
        String username = form.getUsername();
        String password = SecureUtil.md5(form.getPassword());
        SysUserDO sysUser = sysUserService.getByUsernameAndPassword(username, password);
        if (null == sysUser) {
            return MyResult.error("账号或密码错误");
        } else {
            if (sysUser.getUserStatus() == 0) {
                throw new MyException("用户已被禁用");
            }
            Long userId = sysUser.getUserId();
            SysUserLoginDTO dto = new SysUserLoginDTO();
            StpUtil.login(userId);
            dto.setName(sysUser.getName());
            dto.setToken(StpUtil.getTokenValue());
            dto.setUsername(sysUser.getUsername());
            List<SysMenuDTO> menuList = sysMenuService.sysMenuList(true);
            List<String> perms = sysUserService.getBaseMapper().queryPerms(userId);
            List<String> permList = new ArrayList<>();
            perms.stream().filter(StrUtil::isNotBlank).map(s -> StrUtil.split(s, ";")).forEach(permList::addAll);
            dto.setMenuList(menuList);
            dto.setPermList(permList);
            List<SysRoleDO> roles = sysUserService.getBaseMapper().queryRoles(userId);
            dto.setRoleList(roles.stream().map(SysRoleDO::getRoleName).collect(Collectors.toList()));

            SysUserBO sysUserBO = new SysUserBO();
            sysUserBO.setUserId(sysUser.getUserId());
            sysUserBO.setUsername(sysUser.getUsername());
            sysUserBO.setName(sysUser.getName());
            sysUserBO.setDeptId(sysUser.getDeptId());
            StpUtil.getSession().set("user", sysUserBO);

            return MyResult.data(dto);
        }
    }

    public MyResult logout() {
        StpUtil.logout();
        return MyResult.ok("用户退出成功");
    }


}
