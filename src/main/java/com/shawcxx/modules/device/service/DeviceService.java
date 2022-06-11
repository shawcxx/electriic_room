package com.shawcxx.modules.device.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shawcxx.common.exception.MyException;
import com.shawcxx.common.utils.MyUserUtil;
import com.shawcxx.modules.device.bo.DeviceEnum;
import com.shawcxx.modules.device.dao.DeviceDAO;
import com.shawcxx.modules.device.domain.DeviceDO;
import com.shawcxx.modules.device.dto.DeviceDTO;
import com.shawcxx.modules.device.form.DeviceRequestForm;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cjl
 * @date 2022/5/17 11:00
 * @description
 */
@Service
public class DeviceService extends ServiceImpl<DeviceDAO, DeviceDO> {
    @Resource
    private DeviceTemperatureService deviceTemperatureService;

    public void saveDevice(DeviceDO deviceDO) {
        if (StrUtil.isNotBlank(deviceDO.getImei())) {
            LambdaQueryWrapper<DeviceDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DeviceDO::getImei, deviceDO.getImei());
            if (this.count(queryWrapper) > 0) {
                throw new MyException("imei:" + deviceDO.getImei() + ",已被使用");
            }
        }
        this.save(deviceDO);
    }

    public DeviceDO getByImei(String imei) {
        LambdaQueryWrapper<DeviceDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeviceDO::getImei, imei);
        return this.getOne(queryWrapper);
    }


    public void removeByAddressId(String addressId) {
        LambdaQueryWrapper<DeviceDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeviceDO::getAddressId, addressId);
        this.remove(queryWrapper);
    }


    public Page<DeviceDO> deviceList(DeviceRequestForm form) {
        LambdaQueryWrapper<DeviceDO> queryWrapper = new LambdaQueryWrapper<>();
        Long deptId = MyUserUtil.getDeptId();
        queryWrapper.eq(deptId != null, DeviceDO::getDeptId, deptId);
        Page<DeviceDO> page = this.page(new Page<>(form.getCurrent(), form.getSize()), queryWrapper);
        return page;
    }
}
