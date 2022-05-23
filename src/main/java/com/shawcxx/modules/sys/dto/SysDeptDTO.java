package com.shawcxx.modules.sys.dto;

import cn.hutool.core.bean.BeanUtil;
import com.shawcxx.modules.sys.domain.SysDeptDO;
import lombok.Data;

import java.util.Date;

/**
 * @author cjl
 * @date 2022/5/5 9:31
 * @description
 */
@Data
public class SysDeptDTO {

    private Long deptId;

    private String deptName;

    private String manager;
    private String phone;
    private String address;
    private Long projectNum;
    private Long deviceNum;

    public SysDeptDTO(SysDeptDO sysDeptDO) {
        BeanUtil.copyProperties(sysDeptDO, this);
    }
}
