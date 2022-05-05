package com.shawcxx.modules.sys.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shawcxx.common.exception.MyException;
import com.shawcxx.modules.sys.dao.SysDeptDAO;
import com.shawcxx.modules.sys.domain.SysDeptDO;
import com.shawcxx.modules.sys.dto.SysDeptDTO;
import com.shawcxx.modules.sys.form.SysDeptForm;
import com.shawcxx.modules.sys.form.SysDeptQueryForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * @author cjl
 * @date 2022/5/5 9:29
 * @description
 */
@Service
public class SysDeptService extends ServiceImpl<SysDeptDAO, SysDeptDO> {
    public Page<SysDeptDTO> queryPage(SysDeptQueryForm form) {
        Page<SysDeptDO> page = this.page(new Page<>(form.getCurrent(), form.getCurrent()));
        Page<SysDeptDTO> rPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        rPage.setRecords(page.getRecords().stream().map(SysDeptDTO::new).collect(Collectors.toList()));
        return rPage;
    }

    @Transactional(rollbackFor = Exception.class)
    public void del(Long id) {
        this.removeById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(SysDeptForm form) {
        SysDeptDO sysDeptDO = new SysDeptDO();
        LambdaQueryWrapper<SysDeptDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDeptDO::getDeptName, form.getDeptName());
        SysDeptDO one = this.getOne(queryWrapper);
        if (one != null && !form.getDeptId().equals(one.getDeptId())) {
            throw new MyException("运营商已存在");
        }
        BeanUtil.copyProperties(form, sysDeptDO);
        this.saveOrUpdate(sysDeptDO);
    }
}
