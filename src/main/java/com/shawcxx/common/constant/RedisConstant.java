package com.shawcxx.common.constant;

/**
 * @author: dj
 * @create: 2020-10-29 14:12
 * @description: redis静态变量
 */
public class RedisConstant {

    /**
     * 基础redis头
     */
    public static final String BASE_SYS_NAME = "electricRoom";

    /**
     * 验证码token
     */
    public static final String KEY_VERIFY_CODE_TOKEN = BASE_SYS_NAME + ":verifyCodeToken:";

    /**
     * 权限列表
     */
    public static final String KEY_QUERY_ROLE_LIST = BASE_SYS_NAME + ":roleList:";

    public static final String KEY_GET_USER_PERM_LIST = BASE_SYS_NAME + ":permList:";

    /**
     * 找回电话缓存
     */
    public static final String RETRIEVE_KEY = BASE_SYS_NAME + ":retrieveKey:";

    public static final String USER_REMINDER_KEY = BASE_SYS_NAME + ":user:reminder:";


    public static String getKeyVerifyCodeToken(String code) {
        return KEY_VERIFY_CODE_TOKEN + code;
    }

    public static String getRetrieveKey(String mobile) {
        return RETRIEVE_KEY + mobile;
    }

    public static String getRoleListKey(Long userId) {
        return KEY_QUERY_ROLE_LIST + userId;
    }

    public static String getPermListKey(Long userId) {
        return KEY_GET_USER_PERM_LIST + userId;
    }
}
