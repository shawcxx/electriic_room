package com.shawcxx.modules.sys.form;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author: dj
 * @create: 2020-03-04 11:26
 * @description:
 */
@Data
public class SysMenuForm implements Serializable {

    /**
     * 菜单id
     */
    private Long menuId;

    /**
     * 父菜单id
     */
    @NotNull(message = "上级菜单不能为空")
    private Long parentId;

    /**
     * 菜单名称
     */
    @Size(max = 30, message = "菜单名称长度不能超过30位")
    @NotBlank(message = "菜单名称不能为空")
    private String name;

    /**
     * 菜单地址
     */
    @Size(max = 100, message = "菜单地址长度不能超过100位")
    @NotBlank(message = "菜单地址不能为空")
    private String url;

    /**
     * 授权
     */
    @Size(max = 255, message = "授权长度不能超过255位")
    private String perms;

    /**
     * 菜单类型
     */
    private Long type;


    /**
     * 图标
     */
    @Size(max = 20, message = "图标长度过长")
    private String icon;

    /**
     * 排序
     */
    @Max(value = 128, message = "排序大小过大")
    private Integer sort;

    @NotNull(message = "父菜单不能为空")
    private List<Long> parentList;

}
