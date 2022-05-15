package com.shawcxx.modules.device.bo;

/**
 * @author Chen jl
 * @date 2022/5/15 13:59
 * @description
 **/
public enum DeviceEnum {
    /**
     *
     */
    DEVICE_2001("回路测控终端", 2001),
    DEVICE_2002("工业边缘网关", 2002),
    DEVICE_2003("温湿度采集器", 2003),
    DEVICE_2004("测温接收终端", 2004),
    ;

    private DeviceEnum(String deviceName, Integer deviceType) {
        this.deviceName = deviceName;
        this.deviceType = deviceType;
    }

    private final String deviceName;
    private final Integer deviceType;

    public static DeviceEnum getByName(String deviceName) {
        for (DeviceEnum value : values()) {
            if (value.getDeviceName().equals(deviceName)) {
                return value;
            }
        }
        return null;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public String getDeviceName() {
        return deviceName;
    }
}
