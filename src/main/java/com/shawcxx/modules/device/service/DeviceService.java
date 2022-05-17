package com.shawcxx.modules.device.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shawcxx.common.exception.MyException;
import com.shawcxx.modules.device.bo.DeviceEnum;
import com.shawcxx.modules.device.dao.DeviceDAO;
import com.shawcxx.modules.device.domain.DeviceDO;
import org.springframework.stereotype.Service;

/**
 * @author cjl
 * @date 2022/5/17 11:00
 * @description
 */
@Service
public class DeviceService extends ServiceImpl<DeviceDAO, DeviceDO> {
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
}
