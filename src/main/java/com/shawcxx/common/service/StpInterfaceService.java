package com.shawcxx.common.service;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.shawcxx.common.constant.RedisConstant;
import com.shawcxx.common.constant.SysConstant;
import com.shawcxx.modules.sys.domain.SysMenuDO;
import com.shawcxx.modules.sys.domain.SysRoleDO;
import com.shawcxx.modules.sys.service.SysMenuService;
import com.shawcxx.modules.sys.service.SysRoleService;
import com.shawcxx.modules.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: dj
 * @create: 2021-08-31 15:20
 * @description:
 **/
@Slf4j
@Service
public class StpInterfaceService implements StpInterface {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private RedisService redisService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> permsList = null;
        long userId = Long.parseLong((String) loginId);
        String key = RedisConstant.getPermListKey(userId);
        String data = redisService.getValue(key);
        if (StrUtil.isNotBlank(data)) {
            try {
                permsList = JSONObject.parseArray(data, String.class);
            } catch (Exception e) {
                log.warn("{}数据错误:{}", key, data);
            }
        }
        if (CollUtil.isNotEmpty(permsList)) {
            //系统管理员，拥有最高权限
            if (userId == SysConstant.SUPER_ADMIN) {
                List<SysMenuDO> menuList = sysMenuService.list(null);
                permsList = new ArrayList<>();
                menuList.stream().map(SysMenuDO::getPerms).filter(StrUtil::isNotBlank).map(s -> StrUtil.split(s, ";")).forEach(permsList::addAll);
            } else {
                List<String> perms = sysUserService.getBaseMapper().queryPerms(userId);
                permsList = new ArrayList<>();
                perms.stream().filter(StrUtil::isNotBlank).map(s -> StrUtil.split(s, ";")).forEach(permsList::addAll);
            }
            redisService.putValue(key, JSONObject.toJSONString(permsList), 10, TimeUnit.MINUTES);
        }
        return permsList;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> roleList = null;
        long userId = Long.parseLong((String) loginId);
        String key = RedisConstant.getRoleListKey(userId);
        String data = redisService.getValue(key);
        if (StrUtil.isNotBlank(data)) {
            try {
                roleList = JSONObject.parseArray(data, String.class);
            } catch (Exception e) {
                log.warn("{}数据错误:{}", key, data);
            }
        }
        if (null == roleList || 0 == roleList.size()) {
            if (userId == SysConstant.SUPER_ADMIN) {
                List<SysRoleDO> sysRoleList = sysRoleService.list(null);
                roleList = sysRoleList.stream().map(SysRoleDO::getRoleCode).collect(Collectors.toList());
            } else {
                roleList = sysUserService.getBaseMapper().queryRoles(userId).stream().map(SysRoleDO::getRoleCode).collect(Collectors.toList());
            }
        }
        redisService.putValue(key, JSONObject.toJSONString(roleList), 10, TimeUnit.MINUTES);
        return roleList;
    }
}
