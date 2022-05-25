package com.shawcxx.modules.device.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author cjl
 * @date 2022/5/19 9:51
 * @description
 */
@Data
@TableName("t_device_temperature")
public class DeviceTemperatureDO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long sensorId;
    private Double temperature;
    private Date deviceTime;

}
