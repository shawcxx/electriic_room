package com.shawcxx.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 系统日志，切面处理类
 *
 * @author dj
 */
@Slf4j
@Aspect
@Component
public class SysLogAspect {


//    @Resource
//    private SysLogService sysLogService;


    @Pointcut("@annotation(com.shawcxx.common.annotation.SysLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        saveSysLog(point, time);

        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//
//        SysLogDO sysLog = new SysLogDO();
//        SysLog syslog = method.getAnnotation(SysLog.class);
//        if (syslog != null) {
//            //注解上的描述
//            sysLog.setOperation(syslog.value());
//        }
//
//        //请求的方法名
//        String className = joinPoint.getTarget().getClass().getName();
//        String methodName = signature.getName();
//        sysLog.setMethod(className + "." + methodName + "()");
//
//        //请求的参数
//        Object[] args = joinPoint.getArgs();
//        try {
//            String params = JSON.toJSONString(args[0]);
//            sysLog.setParams(params);
//        } catch (Exception e) {
//            log.debug("参数获取错误");
//        }
//
//        //获取request
//        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
////        String requestUrl = request.getRequestURL().toString();
////        requestUrl = StrUtil.subAfter(requestUrl, contextPath, true);
//
//        //设置IP地址
//        sysLog.setIp(MyIPUtil.getIpAddr(request));
//
//        //用户名
//        if (StpUtil.isLogin()) {
//            SysUserBO user = MyUserUtil.getUser();
//            sysLog.setUsername(user.getUsername());
//        }
//        sysLog.setOperationTime(time);
//
//        //保存系统日志
//        sysLogService.save(sysLog);
    }

}
