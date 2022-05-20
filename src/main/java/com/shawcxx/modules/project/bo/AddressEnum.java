package com.shawcxx.modules.project.bo;

import com.shawcxx.modules.device.bo.DeviceEnum;

/**
 * @author Chen jl
 * @date 2022/5/15 13:59
 * @description
 **/
public enum AddressEnum {
    /**
     *
     */
    ROOM("配电房", 1001),
    CABINET("配电柜", 1002),
    LINE("线路", 1003),
    ;

    private AddressEnum(String addressName, Integer addressType) {
        this.addressName = addressName;
        this.addressType = addressType;
    }

    private final String addressName;
    private final Integer addressType;


    public Integer getAddressType() {
        return addressType;
    }

    public String getAddressName() {
        return addressName;
    }

    public static AddressEnum getByName(String addressName) {
        for (AddressEnum value : values()) {
            if (value.getAddressName().equals(addressName)) {
                return value;
            }
        }
        return null;
    }

    public static AddressEnum getByType(Integer addressType) {
        for (AddressEnum value : values()) {
            if (value.getAddressType().equals(addressType)) {
                return value;
            }
        }
        return null;
    }
}
