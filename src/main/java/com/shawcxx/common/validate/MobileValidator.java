package com.shawcxx.common.validate;

import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author cjl
 * @date 2021/7/29 10:11
 * @description
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {
    @Override
    public void initialize(Mobile constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //若为空,跳过,用@NotBlank检验
        if (StrUtil.isBlank(value)) {
            return true;
        }
        return PhoneUtil.isMobile(value);
    }
}
