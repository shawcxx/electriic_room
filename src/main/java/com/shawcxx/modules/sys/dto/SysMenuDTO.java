package com.shawcxx.modules.sys.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.shawcxx.modules.sys.domain.SysMenuDO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author: dj
 * @create: 2020-03-04 15:48
 * @description:
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMenuDTO implements Serializable {

    /**
     * 菜单id
     */
    private Long menuId;

    /**
     * 父菜单id
     */
    private Long parentId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单地址
     */
    private String url;

    /**
     * 授权
     */
    private String perms;

    /**
     * 菜单类型
     */
    private Long type;


    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sort;

    private List<?> list;

    private List<Long> parentList;

    public SysMenuDTO(SysMenuDO sysMenuDO) {
        BeanUtil.copyProperties(sysMenuDO, this);
        if (StrUtil.isNotBlank(sysMenuDO.getParentArray())) {
            this.parentList = JSONArray.parseArray(sysMenuDO.getParentArray()).toJavaList(Long.class);
        }
    }
}
