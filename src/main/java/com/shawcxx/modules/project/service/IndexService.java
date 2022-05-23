package com.shawcxx.modules.project.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shawcxx.common.constant.SysConstant;
import com.shawcxx.common.utils.MyUserUtil;
import com.shawcxx.modules.device.dao.DeviceDAO;
import com.shawcxx.modules.device.domain.DeviceDO;
import com.shawcxx.modules.project.dto.IndexDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Chen jl
 * @date 2022/5/21 16:37
 * @description
 **/
@Service
public class IndexService {

    @Resource
    private DeviceDAO deviceDAO;

    public IndexDTO index() {
        IndexDTO indexDTO = new IndexDTO();
        Long deptId = MyUserUtil.getDeptId();
        LambdaQueryWrapper<DeviceDO> queryWrapper = new LambdaQueryWrapper<DeviceDO>().eq(deptId != null, DeviceDO::getDeptId, deptId).lt(DeviceDO::getDeviceType, 3000);
        List<DeviceDO> deviceList = deviceDAO.selectList(queryWrapper);
        indexDTO.setDeviceNum(Long.parseLong(deviceList.size() + ""));
        indexDTO.setOnlineNum(deviceList.stream().filter(deviceDO -> deviceDO.getDeviceLastTime() != null && deviceDO.getDeviceLastTime().after(DateUtil.offsetHour(DateUtil.date(), SysConstant.OFFLINE_TIME))).count());
        indexDTO.setOfflineNum(indexDTO.getDeviceNum() - indexDTO.getOnlineNum());
        indexDTO.setAlarmNum(0L);
        indexDTO.setUnDisposeNum(0L);
        return indexDTO;
    }
}
