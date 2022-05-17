package com.shawcxx.modules.device.service;

import com.shawcxx.modules.project.service.AddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    }
}
