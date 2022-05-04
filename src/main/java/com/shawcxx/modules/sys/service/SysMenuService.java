package com.shawcxx.modules.sys.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shawcxx.common.base.MyResult;
import com.shawcxx.common.constant.SysConstant;
import com.shawcxx.common.exception.MyException;
import com.shawcxx.modules.sys.dao.SysMenuDAO;
import com.shawcxx.modules.sys.domain.SysMenuDO;
import com.shawcxx.modules.sys.domain.SysRoleMenuDO;
import com.shawcxx.modules.sys.dto.SysMenuDTO;
import com.shawcxx.modules.sys.form.SysMenuForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: dj
 * @create: 2020-10-29 14:02
 * @description:
 */
@Service
public class SysMenuService extends ServiceImpl<SysMenuDAO, SysMenuDO> {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @Transactional(rollbackFor = Exception.class)
    public MyResult saveOrUpdate(SysMenuForm form) {

        //菜单
        if (form.getType() == SysConstant.MenuType.MENU.getValue()) {
            if (StrUtil.isBlank(form.getUrl())) {
                throw new MyException("菜单URL不能为空");
            }
        }

        //上级菜单类型
        int parentType = SysConstant.MenuType.CATALOG.getValue();
        if (form.getParentId() != 0) {
            SysMenuDO dto = this.getById(form.getParentId());
            parentType = dto.getType();
        }

        //目录、菜单
        if (form.getType() == SysConstant.MenuType.CATALOG.getValue() ||
                form.getType() == SysConstant.MenuType.MENU.getValue()) {
            if (parentType != SysConstant.MenuType.CATALOG.getValue()) {
                throw new MyException("上级菜单只能为目录类型");
            }
        }

        //按钮
        if (form.getType() == SysConstant.MenuType.BUTTON.getValue()) {
            if (parentType != SysConstant.MenuType.MENU.getValue()) {
                throw new MyException("上级菜单只能为菜单类型");
            }
        }
        //接口
        if (form.getType() == SysConstant.MenuType.API.getValue()) {
            if (parentType != SysConstant.MenuType.MENU.getValue()) {
                throw new MyException("上级菜单只能为菜单类型");
            }
        }
        SysMenuDO sysMenuDO = new SysMenuDO();
        BeanUtil.copyProperties(form, sysMenuDO);
        if (null != form.getParentList()) {
            sysMenuDO.setParentArray(JSON.toJSONString(form.getParentList()));
        }
        this.saveOrUpdate(sysMenuDO);
        return MyResult.ok("保存成功");
    }

    public List<SysMenuDTO> sysMenuList(boolean ignoreButton) {
        long userId = StpUtil.getLoginIdAsLong();
        //管理员权限账户获取到的菜单列表
        if (userId == SysConstant.SUPER_ADMIN) {
            //管理员权限账户获取到的菜单列表
            return getMenuTreeList(getMenuIdList(null), ignoreButton);
        } else {
            return getMenuTreeList(getMenuIdList(userId), ignoreButton);
        }
    }

    public SysMenuDTO getMenuSelectTree() {
        //查询列表数据
        List<SysMenuDTO> sysMenuDOList = this.getMenuTreeList(getMenuIdList(null), true);
        //添加顶级菜单
        SysMenuDTO root = new SysMenuDTO();
        root.setMenuId(0L);
        root.setName("首页");
        root.setParentId(-1L);
        root.setList(sysMenuDOList);
        return root;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long menuId) {
        //删除菜单
        this.removeById(menuId);
        //删除菜单与角色关联
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenuDO>().eq("menu_id", menuId));
    }

    private Set<Long> getMenuIdList(Long userId) {
        List<Long> menuIdList;
        if (userId == null || userId == SysConstant.SUPER_ADMIN) {
            menuIdList = this.list().stream().map(SysMenuDO::getMenuId).collect(Collectors.toList());
        } else {
            menuIdList = sysUserService.getBaseMapper().queryAllMenuId(userId);
        }
        return new HashSet<>(menuIdList);
    }

    private List<SysMenuDTO> getMenuTreeList(Set<Long> menuIdList, boolean ignoreButton) {
        QueryWrapper<SysMenuDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        queryWrapper.lt(ignoreButton, "type", SysConstant.MenuType.BUTTON.getValue());
        List<SysMenuDO> list = this.list(queryWrapper);
        List<SysMenuDTO> menuList = list.stream().map(SysMenuDTO::new).collect(Collectors.toList());
        List<SysMenuDTO> iniList = menuList.stream().filter(sysMenuDTO -> sysMenuDTO.getParentId() == 0 && menuIdList.contains(sysMenuDTO.getMenuId())).collect(Collectors.toList());
        Map<Long, List<SysMenuDTO>> map = new HashMap<>();
        for (SysMenuDTO sysMenuDTO : menuList) {
            if (menuIdList != null && !menuIdList.contains(sysMenuDTO.getMenuId())) {
                continue;
            }
            List<SysMenuDTO> childList = map.computeIfAbsent(sysMenuDTO.getParentId(), k -> new ArrayList<>());
            childList.add(sysMenuDTO);
        }
        this.recursion(iniList, map);
        return iniList;
    }

    /**
     * 递归查询
     *
     * @param iniList 最上级菜单
     * @param map     key: 菜单父级id,value: 菜单list
     */
    private void recursion(List<SysMenuDTO> iniList, Map<Long, List<SysMenuDTO>> map) {
        for (SysMenuDTO sysMenuDTO : iniList) {
            Long kye = sysMenuDTO.getMenuId();
            List<SysMenuDTO> list = map.get(kye);
            if (CollUtil.isNotEmpty(list)) {
                sysMenuDTO.setList(list);
                recursion(list, map);
            }
        }
    }
}
