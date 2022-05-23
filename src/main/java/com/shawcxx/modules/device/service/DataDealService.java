package com.shawcxx.modules.device.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shawcxx.modules.device.bo.DeviceEnum;
import com.shawcxx.modules.device.domain.DeviceDO;
import com.shawcxx.modules.device.domain.DeviceTemperatureDO;
import com.shawcxx.modules.project.domain.AddressDO;
import com.shawcxx.modules.project.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cjl
 * @date 2022/5/17 13:59
 * @description
 */
@Service
@Slf4j
public class DataDealService {
    @Resource
    private DeviceService deviceService;

    @Resource
    private DeviceTemperatureService deviceTemperatureService;

    @Resource
    private AddressService addressService;

    public void dealData(String record) {
        JSONObject json = JSONObject.parseObject(record);
        String imei = json.getString("IMEI");
        Long time = json.getLong("time") * 1000;
        JSONArray jsonArray = json.getJSONArray("data");
        DeviceDO device = deviceService.getByImei(imei);
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                JSONObject data = jsonArray.getJSONObject(i);
                JSONArray temp2 = data.getJSONArray("temp2");
                String addr = data.getString("addr");
                List<DeviceDO> list = deviceService.getDeviceList(imei, addr, DeviceEnum.DEVICE_3001.getDeviceType());
                List<DeviceTemperatureDO> temperatureList = new ArrayList<>();
                for (int j = 0; j < list.size(); j++) {
                    DeviceDO deviceDO = list.get(j);
                    Double temperature = CollUtil.get(temp2, j) == null ? null : temp2.getDouble(j);
                    if (temperature != null) {
                        DeviceTemperatureDO deviceTemperatureDO = new DeviceTemperatureDO();
                        deviceTemperatureDO.setDeviceId(deviceDO.getDeviceId());
                        deviceTemperatureDO.setTemperature(temperature);
                        deviceTemperatureDO.setDeviceTime(DateUtil.date(time));
                        temperatureList.add(deviceTemperatureDO);
                    }
                }
                if (CollUtil.isNotEmpty(temperatureList)) {
                    deviceTemperatureService.saveBatch(temperatureList);
                }
            } catch (Exception e) {
                log.error("数据处理失败", e);
            }
        }
        deviceService.update(new LambdaUpdateWrapper<DeviceDO>().set(DeviceDO::getDeviceLastTime, DateUtil.date()).eq(DeviceDO::getAddressId, device.getAddressId()));
    }


}
