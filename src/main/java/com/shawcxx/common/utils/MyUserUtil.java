package com.shawcxx.common.utils;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import com.shawcxx.common.base.SysUserBO;
import com.shawcxx.common.exception.MyException;

/**
 * @author cjl
 * @date 2022/3/2 13:38
 * @description
 */
public class MyUserUtil {

    public static SysUserBO getUser() {
        SysUserBO user = StpUtil.getSession().getModel("user", SysUserBO.class);
        if (user == null) {
            throw new MyException("尚未登录,请先登录");
        }
        return user;
    }


}

