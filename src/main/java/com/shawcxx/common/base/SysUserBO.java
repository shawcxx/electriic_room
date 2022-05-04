package com.shawcxx.common.base;

import lombok.Data;

/**
 * @author cjl
 * @date 2022/5/4 9:27
 * @description
 */
@Data
public class SysUserBO {
    private Long userId;
    private String username;
    private String name;
    private Long deptId;
}
