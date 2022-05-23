package com.shawcxx.modules.sys.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shawcxx.common.exception.MyException;
import com.shawcxx.modules.device.dao.DeviceDAO;
import com.shawcxx.modules.device.domain.DeviceDO;
import com.shawcxx.modules.project.dao.ProjectDAO;
import com.shawcxx.modules.project.domain.ProjectDO;
import com.shawcxx.modules.sys.dao.SysDeptDAO;
import com.shawcxx.modules.sys.domain.SysDeptDO;
import com.shawcxx.modules.sys.dto.SysDeptDTO;
import com.shawcxx.modules.sys.form.SysDeptForm;
import com.shawcxx.modules.sys.form.SysDeptQueryForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cjl
 * @date 2022/5/5 9:29
 * @description
 */
@Service
public class SysDeptService extends ServiceImpl<SysDeptDAO, SysDeptDO> {
    @Resource
    private DeviceDAO deviceDAO;
    @Resource
    private ProjectDAO projectDAO;

    public Page<SysDeptDTO> queryPage(SysDeptQueryForm form) {
        Page<SysDeptDO> page = this.page(new Page<>(form.getCurrent(), form.getSize()));
        Page<SysDeptDTO> rPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<SysDeptDTO> list = new ArrayList<>();
        for (SysDeptDO sysDeptDO : page.getRecords()) {
            SysDeptDTO sysDeptDTO = new SysDeptDTO(sysDeptDO);
            sysDeptDTO.setDeviceNum(deviceDAO.selectCount(new LambdaQueryWrapper<DeviceDO>().eq(DeviceDO::getDeptId, sysDeptDO.getDeptId()).lt(DeviceDO::getDeviceType, 3000)));
            sysDeptDTO.setProjectNum(projectDAO.selectCount(new LambdaQueryWrapper<ProjectDO>().eq(ProjectDO::getDeptId, sysDeptDO.getDeptId())));
            list.add(sysDeptDTO);
        }
        rPage.setRecords(list);
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
            throw new MyException("运营商名称已存在");
        }
        BeanUtil.copyProperties(form, sysDeptDO);
        this.saveOrUpdate(sysDeptDO);
    }

    public SysDeptDO findOrCreate(String deptName) {
        LambdaQueryWrapper<SysDeptDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDeptDO::getDeptName, deptName);
        SysDeptDO deptDO = this.getOne(queryWrapper);
        if (deptDO == null) {
            deptDO = new SysDeptDO();
            deptDO.setDeptName(deptName);
            this.save(deptDO);
        }
        return deptDO;
    }
}
