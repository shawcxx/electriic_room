package com.shawcxx.modules.sys.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shawcxx.common.exception.MyException;
import com.shawcxx.modules.sys.dao.SysUserDAO;
import com.shawcxx.modules.sys.domain.SysRoleDO;
import com.shawcxx.modules.sys.domain.SysUserDO;
import com.shawcxx.modules.sys.dto.SysUserDTO;
import com.shawcxx.modules.sys.form.SysUserForm;
import com.shawcxx.modules.sys.form.SysUserQueryForm;
import lombok.val;
import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cjl
 * @create 2020/3/3
 */
@Service
public class SysUserService extends ServiceImpl<SysUserDAO, SysUserDO> {
    @Resource
    private SysUserRoleService sysUserRoleService;


    public Page<SysUserDTO> query(SysUserQueryForm form) {
        var username = form.getUsername();
        var name = form.getName();
        LambdaQueryWrapper<SysUserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(username), SysUserDO::getUsername, username);
        queryWrapper.like(StrUtil.isNotBlank(name), SysUserDO::getName, name);
        Page<SysUserDO> page = this.page(new Page<>(form.getCurrent(), form.getSize()), queryWrapper);
        Page<SysUserDTO> rpage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        rpage.setRecords(page.getRecords().stream().map(SysUserDTO::new).collect(Collectors.toList()));
        return rpage;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(SysUserForm form) {
        val username = form.getUsername();
        QueryWrapper<SysUserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);

        SysUserDO sysUserDO = new SysUserDO();
        if (form.getUserId() == null) {
            SysUserDO one = this.getOne(queryWrapper);
            if (one != null) {
                throw new MyException("账号已经存在");
            }
            sysUserDO.setUsername(username);
            sysUserDO.setPassword(SecureUtil.md5(form.getPassword()));
        } else {
            sysUserDO.setUserId(form.getUserId());
        }
        sysUserDO.setName(form.getName());
        sysUserDO.setUserStatus(form.getUserStatus());
        this.saveOrUpdate(sysUserDO);

        List<Long> roleIdList = form.getRoleIdList();
        Long userId = sysUserDO.getUserId();
        sysUserRoleService.saveOrUpdate(userId, roleIdList);
    }


    public SysUserDTO info(Long userId) {
        SysUserDO user = getById(userId);
        SysUserDTO sysUserDTO = new SysUserDTO(user);
        if (null != user) {
            //获取用户所属的角色列表
            List<SysRoleDO> roles = baseMapper.queryRoles(userId);
            sysUserDTO.setRoleIdList(roles.stream().map(SysRoleDO::getRoleId).collect(Collectors.toList()));
        }
        return sysUserDTO;
    }

    public SysUserDO getByUsernameAndPassword(String username, String password) {
        LambdaQueryWrapper<SysUserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserDO::getUsername, username);
        queryWrapper.eq(SysUserDO::getPassword, password);
        return this.getOne(queryWrapper);
    }
}
