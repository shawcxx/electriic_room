package com.shawcxx.common.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

/**
 * @author cjl
 * @date 2021/7/29 10:11
 * @description
 */
@Documented
@Target({ METHOD, FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {MobileValidator.class})
public @interface Mobile {
    String message() default "手机号长度不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
