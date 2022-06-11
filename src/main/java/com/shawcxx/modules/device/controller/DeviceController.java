package com.shawcxx.modules.device.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shawcxx.common.base.MyResult;
import com.shawcxx.modules.device.domain.DeviceDO;
import com.shawcxx.modules.device.form.DeviceRequestForm;
import com.shawcxx.modules.device.service.DeviceService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

    @PostMapping("list")
    public MyResult list(@RequestBody DeviceRequestForm form) {
        Page<DeviceDO> page = deviceService.deviceList(form);
        return MyResult.data(page);
    }
}
