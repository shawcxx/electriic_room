package com.shawcxx.modules.device.service;

import com.alibaba.fastjson.JSONObject;
import com.shawcxx.modules.device.domain.DeviceDO;
import com.shawcxx.modules.project.domain.AddressDO;
import com.shawcxx.modules.project.service.AddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author cjl
 * @date 2022/5/17 13:59
 * @description
 */
@Service
public class DataDealService {
    @Resource
    private DeviceService deviceService;
    @Resource
    private AddressService addressService;


    public void dealData(String data) {
        JSONObject json = JSONObject.parseObject(data);
        String imei = json.getString("imei");
        DeviceDO deviceDO = deviceService.getByImei(imei);
        if (deviceDO != null) {
            AddressDO addressDO = addressService.getById(deviceDO.getAddressId());

        }
    }


}
