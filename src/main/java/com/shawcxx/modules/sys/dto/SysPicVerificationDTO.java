package com.shawcxx.modules.sys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author cjl
 * @create 2020/3/11
 * 图片验证码返回封装类
 */
@Data
@AllArgsConstructor
public class SysPicVerificationDTO {
    private String verifyCodeToken;
    private String pic;
}
