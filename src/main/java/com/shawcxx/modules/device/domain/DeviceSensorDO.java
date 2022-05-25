package com.shawcxx.modules.device.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author Chen jl
 * @date 2022/5/25 19:24
 * @description
 **/
@Data
@TableName("t_device_sensor")
public class DeviceSensorDO {
    @TableId
    private Long sensorId;

    private String sensorName;
    private String addressId;
    private String projectId;
    private Integer sensorType;
    private Long deptId;
    private String imei;
    private String modbus;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date createTime;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date updateTime;

}
