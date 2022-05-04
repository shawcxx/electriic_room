package com.shawcxx.modules.sys.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: dj
 * @create: 2020-03-04 15:47
 * @description: 用户登录后返回结果
 */
@Data
public class SysUserLoginDTO implements Serializable {

    private String token;

    private String username;

    private String name;

    private List<SysMenuDTO> menuList;

    private List<String> permList;

    private List<String> roleList;

}
