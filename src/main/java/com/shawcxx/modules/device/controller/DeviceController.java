package com.shawcxx.modules.device.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.shawcxx.common.base.MyResult;
import com.shawcxx.modules.device.dto.DeviceDTO;
import com.shawcxx.modules.device.service.DeviceService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author cjl
 * @date 2022/5/20 16:52
 * @description
 */
@RestController
@RequestMapping("device")
public class DeviceController {
    @Resource
    private DeviceService deviceService;


    @PostMapping("sensorList")
    @SaCheckLogin
    public MyResult sensorList(@RequestParam String addressId) {
        List<DeviceDTO> list = deviceService.sensorList(addressId);
        return MyResult.data(list);
    }
}
