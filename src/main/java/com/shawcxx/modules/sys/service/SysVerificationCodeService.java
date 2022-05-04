package com.shawcxx.modules.sys.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.util.StrUtil;
import com.shawcxx.common.base.MyResult;
import com.shawcxx.common.constant.RedisConstant;
import com.shawcxx.common.exception.MyException;
import com.shawcxx.modules.sys.dto.SysPicVerificationDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author cjl
 * @date 2021/11/6 9:10
 * @description 验证码
 */
@Service
public class SysVerificationCodeService {


    @Value("${verificationCodeTemplateId:64}")
    private String smsTemplateId;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 获取登陆验证码
     */
    public MyResult getVerificationCode(String mobile) {
        if (StrUtil.isNotBlank(mobile)) {
            return getSmsVerificationCode(mobile);
        } else {
            CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(100, 30, 4, 3);
            String text = captcha.getCode().toUpperCase();
            UUID uuid = UUID.randomUUID();
            String verifyCodeTokenKey = RedisConstant.getKeyVerifyCodeToken(uuid.toString());
            setCache(verifyCodeTokenKey, text, 5);
            String pic = captcha.getImageBase64Data();
            return MyResult.data(new SysPicVerificationDTO(uuid.toString(), pic));
        }
    }

    /**
     * 获取手机验证码
     */
    private MyResult getSmsVerificationCode(String mobile) {
//        if (!Validator.isMobile(mobile)) {
//            return MyResult.error("手机号码格式错误,请重新填写");
//        }
//        String retrieveKey = RedisConstant.getRetrieveKey(mobile);
//        String send = getCache(retrieveKey);
//        if (null != send) {
//            return MyResult.error("验证码一分钟内只能获取一次");
//        }
//        String code = RandomUtil.randomNumbers(6);
//        String verifyCodeTokenKey = RedisConstant.getKeyVerifyCodeToken(mobile);
//        SmsBO smsBO = new SmsBO();
//        HashMap<String, String> map = new HashMap<>(0);
//        map.put("code", code);
//        smsBO.setMsg(map);
//        smsBO.setMobile(mobile);
//        if (!smsService.send(smsBO, smsTemplateId)) {
//            throw new MyException("验证码发送失败,请稍后再试");
//        }
//        setCache(verifyCodeTokenKey, code, 5);
//        setCache(retrieveKey, code, 1);
//        return MyResult.data(mobile);
        return MyResult.data("");
    }


    public void check(String verificationCode, String token) {
        if (StrUtil.hasBlank(verificationCode, token)) {
            throw new MyException("验证码不能为空");
        }
        String key = RedisConstant.getKeyVerifyCodeToken(token);
        String code = getCache(key);
        if (StrUtil.isBlank(code)) {
            throw new MyException("验证码已过期");
        }
        if (!verificationCode.equalsIgnoreCase(code)) {
            throw new MyException("验证码不正确");
        } else {
            stringRedisTemplate.delete(key);
        }
    }

    private String getCache(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    private void setCache(String key, String value, long timeout) {
        stringRedisTemplate.opsForValue().set(key, value,
                timeout, TimeUnit.MINUTES);
    }

}
