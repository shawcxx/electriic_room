package com.shawcxx.modules.sys.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shawcxx.common.exception.MyException;
import com.shawcxx.modules.sys.dao.SysRoleDAO;
import com.shawcxx.modules.sys.domain.SysRoleDO;
import com.shawcxx.modules.sys.domain.SysRoleMenuDO;
import com.shawcxx.modules.sys.domain.SysUserRoleDO;
import com.shawcxx.modules.sys.dto.SysMenuDTO;
import com.shawcxx.modules.sys.dto.SysRoleDTO;
import com.shawcxx.modules.sys.form.SysRoleForm;
import com.shawcxx.modules.sys.form.SysRoleQueryForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: dj
 * @create: 2020-03-04 08:57
 * @description:
 */
@Service
public class SysRoleService extends ServiceImpl<SysRoleDAO, SysRoleDO> {
    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource
    private SysMenuService sysMenuService;

    public Page<SysRoleDO> query(SysRoleQueryForm form) {
        String roleName = form.getRoleName();
        LambdaQueryWrapper<SysRoleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(roleName), SysRoleDO::getRoleName, roleName);
        Page<SysRoleDO> page = this.page(new Page<>(form.getCurrent(), form.getSize()), queryWrapper);
        return page;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(SysRoleForm form) {
        SysRoleDO sysRoleDO = new SysRoleDO();
        BeanUtil.copyProperties(form, sysRoleDO);
        if (sysRoleDO.getRoleId() == null) {
            //??????
            SysRoleDO one = baseMapper.selectOne(new QueryWrapper<SysRoleDO>().eq("role_name", sysRoleDO.getRoleName()));
            if (one != null) {
                throw new MyException("??????????????????");
            }
        }
        this.saveOrUpdate(sysRoleDO);

        List<Long> menuIdList = form.getMenuIdList();

        //????????????????????????
        LambdaQueryWrapper<SysRoleMenuDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SysRoleMenuDO::getRoleId, sysRoleDO.getRoleId());
        sysRoleMenuService.remove(lqw);

        //????????????
        if (null != menuIdList && 0 < menuIdList.size()) {
            //??????????????????
            for (Long menuId : form.getMenuIdList()) {
                SysRoleMenuDO sysRoleMenuDO = new SysRoleMenuDO();
                sysRoleMenuDO.setRoleId(sysRoleDO.getRoleId());
                sysRoleMenuDO.setMenuId(menuId);
                sysRoleMenuService.save(sysRoleMenuDO);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long roleId) {
        //????????????
        this.removeById(roleId);
        //???????????????????????????
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenuDO>().eq("role_id", roleId));
        //???????????????????????????
        sysUserRoleService.remove(new QueryWrapper<SysUserRoleDO>().eq("role_id", roleId));
    }

    public SysRoleDTO info(Long roleId) {
        SysRoleDO sysRoleDO = this.getById(roleId);
        SysRoleDTO sysRoleDTO = new SysRoleDTO(sysRoleDO);

        //????????????????????????????????????id
        List<SysMenuDTO> menuList = sysMenuService.sysMenuList(false);
        Set<Long> menuChildIdList = new HashSet<>();
        this.getChildMenuIdList(menuChildIdList, menuList);
        List<Long> menuIdList = new ArrayList<>();

        List<SysRoleMenuDO> sysRoleMenuList = sysRoleMenuService.list(new QueryWrapper<SysRoleMenuDO>().eq("role_id", roleId));
        for (SysRoleMenuDO sysRoleMenuDO : sysRoleMenuList) {
            if (menuChildIdList.contains(sysRoleMenuDO.getMenuId())) {
                menuIdList.add(sysRoleMenuDO.getMenuId());
            }
        }
        sysRoleDTO.setMenuIdList(menuIdList);
        return sysRoleDTO;
    }

    private void getChildMenuIdList(Set<Long> menuIdList, List<SysMenuDTO> menuList) {
        for (SysMenuDTO sysMenuDTO : menuList) {
            List<?> list = sysMenuDTO.getList();
            if (CollectionUtil.isEmpty(list)) {
                menuIdList.add(sysMenuDTO.getMenuId());
            } else {
                this.getChildMenuIdList(menuIdList, (List<SysMenuDTO>) sysMenuDTO.getList());
            }
        }
    }
}
