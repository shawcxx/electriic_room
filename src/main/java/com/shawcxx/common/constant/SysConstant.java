package com.shawcxx.common.constant;

/**
 * @author: dj
 * @create: 2020-10-29 14:12
 * @description: 系统静态变量
 */
public class SysConstant {

    /**
     * 超级管理员ID
     */
    public static final int SUPER_ADMIN = 1;

    /**
     * 管理员角色
     */
    public static final String ADMIN = "admin";

    /**
     * 逻辑删除未删除标识
     */
    public static final Integer UN_DEL_FLAG = 0;

    /**
     * 逻辑删除以删除标识
     */
    public static final Integer DEL_FLAG = 1;
    public static final int OFFLINE_TIME = 72;

    /**
     * 菜单类型
     */
    public enum MenuType {

        /**
         * 目录
         */
        CATALOG(0),

        /**
         * 菜单
         */
        MENU(1),

        /**
         * 按钮
         */
        BUTTON(2),

        /**
         * api
         */
        API(3);

        private final int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

}
