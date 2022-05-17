package com.shawcxx.modules.device.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author cjl
 * @date 2022/5/17 10:58
 * @description
 */
@Data
@TableName("t_device")
public class DeviceDO {
    @TableId
    private Long deviceId;
    private String addressId;
    private String projectId;
    private String deviceName;
    private Integer deviceType;
    private Long deptId;
    private String simCard;
    private String imei;
    private String modbus;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date createTime;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date updateTime;
}
