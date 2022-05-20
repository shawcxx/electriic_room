package com.shawcxx.modules.device.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shawcxx.modules.device.dao.DeviceTemperatureDAO;
import com.shawcxx.modules.device.domain.DeviceDO;
import com.shawcxx.modules.device.domain.DeviceTemperatureDO;
import org.springframework.stereotype.Service;

/**
 * @author cjl
 * @date 2022/5/19 14:38
 * @description
 */
@Service
public class DeviceTemperatureService extends ServiceImpl<DeviceTemperatureDAO, DeviceTemperatureDO> {
    public Double getDeviceLastRecords(Long deviceId) {
        LambdaQueryWrapper<DeviceTemperatureDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeviceTemperatureDO::getDeviceId, deviceId);
        queryWrapper.orderByDesc(DeviceTemperatureDO::getDeviceTime);
        queryWrapper.last("limit 1");
        DeviceTemperatureDO temperatureDO = this.getOne(queryWrapper);
        if (temperatureDO != null) {
            return temperatureDO.getTemperature();
        } else {
            return null;
        }
    }
}
