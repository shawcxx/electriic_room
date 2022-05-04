package com.shawcxx.common.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.exception.NotSafeException;
import cn.hutool.json.JSONException;
import com.shawcxx.common.base.MyResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

/**
 * @author: dj
 * @create: 2020-03-05 09:18
 * @description: 异常处理器
 */
@Slf4j
@RestControllerAdvice
public class MyExceptionHandler {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String fieMaxSize;

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(MyException.class)
    public MyResult handleMyException(MyException e) {
        return new MyResult(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public MyResult handleException(Exception e) {
        if (e instanceof NoHandlerFoundException) {
            return MyResult.noPath();
        } else if (e instanceof DuplicateKeyException) {
            return MyResult.error("数据库中已存在该记录");
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            return MyResult.error("请求类型错误");
        } else if (e instanceof ConstraintViolationException) {
            return MyResult.error("插入数据违反唯一约束");
        } else if (e instanceof NotLoginException) {
            return MyResult.noToken(e.getMessage());
        } else if (e instanceof NotRoleException) {
            return MyResult.error("权限不足");
        } else if (e instanceof NotPermissionException) {
            return MyResult.error("权限错误");
        } else if (e instanceof NotSafeException) {
            return MyResult.noSafe();
        } else if (e instanceof JSONException) {
            return MyResult.error("json格式错误");
        } else if (e instanceof MaxUploadSizeExceededException) {
            return MyResult.unauthorized("上传文件过大,请重新选择小于 " + fieMaxSize + " 文件上传");
        } else if (e instanceof MissingServletRequestParameterException) {
            return MyResult.error("缺少请求参数");
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
            return MyResult.error(Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage());
        } else if (e instanceof BindException) {
            BindException exception = (BindException) e;
            return MyResult.error(Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage());
        } else {
            log.error(e.getMessage(), e);
            return MyResult.error("未知异常,请联系管理员");
        }
    }

}
