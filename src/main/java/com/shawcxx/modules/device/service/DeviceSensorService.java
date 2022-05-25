package com.shawcxx.modules.device.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shawcxx.modules.device.dao.DeviceSensorDAO;
import com.shawcxx.modules.device.domain.DeviceDO;
import com.shawcxx.modules.device.domain.DeviceSensorDO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Chen jl
 * @date 2022/5/25 19:25
 * @description
 **/
@Service
public class DeviceSensorService extends ServiceImpl<DeviceSensorDAO, DeviceSensorDO> {

    public List<DeviceSensorDO> getDeviceList(String imei, String addr, Integer deviceType) {
        LambdaQueryWrapper<DeviceSensorDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeviceSensorDO::getImei, imei);
        queryWrapper.eq(DeviceSensorDO::getModbus, addr);
        queryWrapper.eq(DeviceSensorDO::getSensorType, deviceType);
        queryWrapper.orderByAsc(DeviceSensorDO::getSensorId);
        return this.list(queryWrapper);
    }
}
