package com.shawcxx.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: dj
 * @create: 2020-03-04 11:26
 * @description:
 */
@Data
@TableName("sys_menu")
public class SysMenuDO implements Serializable {

    /**
     * 菜单id
     */
    @TableId(type = IdType.AUTO)
    private Long menuId;

    /**
     * 父菜单id
     */
    private Long parentId;

    /**
     * 父菜单名称
     */
    @TableField(exist = false)
    private String parentName;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单URL
     */
    private String url;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    private String perms;

    /**
     * 类型     0：目录   1：菜单   2：按钮
     */
    private Integer type;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sort;



    private String parentArray;

}
