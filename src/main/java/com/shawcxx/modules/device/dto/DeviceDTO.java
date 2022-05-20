package com.shawcxx.modules.device.dto;

import lombok.Data;

/**
 * @author cjl
 * @date 2022/5/20 16:53
 * @description
 */
@Data
public class DeviceDTO {
    private Long deviceId;
    private String addressId;
    private String projectId;
    private String deviceName;
    private Integer deviceType;
    private Long deptId;
    private Double temperature;
}
