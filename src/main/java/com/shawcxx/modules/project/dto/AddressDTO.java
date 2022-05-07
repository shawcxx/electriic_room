package com.shawcxx.modules.project.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author cjl
 * @date 2022/5/7 10:59
 * @description
 */
@Data
public class AddressDTO {

    private String addressId;
    private Integer addressType;
    private String addressName;
    private String parentAddressId;
    private Date createTime;
    private Date updateTime;
    private List<AddressDTO> child;

}
