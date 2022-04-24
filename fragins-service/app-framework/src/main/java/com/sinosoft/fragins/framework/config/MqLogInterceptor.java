package com.sinosoft.fragins.framework.config;

import com.sinosoft.fragins.framework.exception.ExceptionLogger;
import com.sinosoft.fragins.framework.mq.MqListener;
import com.sinosoft.fragins.framework.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author: Mingze.Li
 * @create: 2021-03-02 17:09
 **/
@Slf4j
@Component
@Aspect
public class MqLogInterceptor {

    ThreadLocal<Long> time = new ThreadLocal<>();

    /**
     * 切面切入点
     */
    @Pointcut("execution(public * com.sinosoft.fragins..*Listener.*(..))")
    public void mqLog() {
        //切面切入
    }

    @Before("mqLog()")
    public void doBefore(JoinPoint joinPoint) {
        try {
            AppContext.set("LOG_UID", UUID.randomUUID().toString().replace("-", ""));

            // 接收到请求，记录请求内容
            time.set(System.currentTimeMillis());
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            MqListener annotation = method.getAnnotation(MqListener.class);

            AppContext.set("MQ_TOPIC", annotation.topic());

            // 记录下请求内容
            log.info("monitor_log||消息||开始||{}||-||-",
                    annotation.topic());
        } catch (Exception e) {
            log.info("日志打印错误");
        }
    }

    @AfterReturning(returning = "ret", pointcut = "mqLog()")
    public void doAfterReturning(Object ret) {
        // 处理完请求，返回内容
        try {
            long timestamp = System.currentTimeMillis() - time.get();
            log.info("monitor_log||消息||成功||{}||{}||{}", AppContext.get("MQ_TOPIC"), timestamp, JsonUtils.stringify(ret));
            time.remove();
            AppContext.remove("LOG_UID");
            AppContext.remove("MQ_TOPIC");
        } catch (Exception e) {
            log.info("日志打印错误");
        }
    }

    @AfterThrowing(throwing = "ex", value = "mqLog()")
    public void afterThrowing(JoinPoint point, Throwable ex) {
        try {
            long timestamp = System.currentTimeMillis() - time.get();
            MethodSignature methodSignature = (MethodSignature) point.getSignature();
            Method method = methodSignature.getMethod();
            MqListener annotation = method.getAnnotation(MqListener.class);
            if (annotation.exceptionLogger()) {
                ExceptionLogger.logAsync(ex, annotation.topic(), point.getArgs()[0].toString());
            }
            log.info("monitor_log||消息||异常||{}||{}||{}:{}", AppContext.get("MQ_TOPIC"), timestamp, ex.getMessage(), ex);
            time.remove();
            AppContext.remove("LOG_UID");
            AppContext.remove("MQ_TOPIC");
        } catch (Exception e) {
            log.info("日志打印错误");
        }
    }


}
