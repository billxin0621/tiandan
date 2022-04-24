package com.sinosoft.fragins.framework.config;

import com.sinosoft.fragins.framework.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author: Mingze.Li
 * @create: 2021-03-02 17:09
 **/
@Slf4j
@Component
@Aspect
public class RequestLogInterceptor {

    ThreadLocal<Long> time = new ThreadLocal<>();

    /**
     * 切面切入点
     */
    @Pointcut("execution(public * com.sinosoft.fragins..*Api.*(..))")
    public void webLog() {
        //切面切入
    }

    /**
     * 获得path部分<br>
     *
     * @param uriStr URI路径
     * @return path
     */
    public static String getPath(String uriStr) {
        URI uri;
        try {
            uri = new URI(uriStr);
        } catch (URISyntaxException e) {
            return uriStr;
        }
        return uri.getPath();
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        // 处理完请求，返回内容
        try {
            long timestamp = System.currentTimeMillis() - time.get();
            log.info("monitor_log||请求||成功||{}||{}||{}", AppContext.get("REQUEST_URL"), timestamp, JsonUtils.stringify(ret));
            time.remove();
            AppContext.remove("LOG_UID");
            AppContext.remove("REQUEST_URL");
        } catch (Exception e) {
            log.info("日志打印错误");
        }
    }

    @AfterThrowing(throwing = "ex", value = "webLog()")
    public void afterThrowing(Throwable ex) {
        try {
            long timestamp = System.currentTimeMillis() - time.get();
            log.info("monitor_log||请求||异常||{}||{}||{}:{}", AppContext.get("REQUEST_URL"), timestamp, ex.getMessage(), ex);
            time.remove();
            AppContext.remove("LOG_UID");
            AppContext.remove("REQUEST_URL");
        } catch (Exception e) {
            log.info("日志打印错误");
        }
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        try {
            String traceId = null;
            try {
                traceId = TraceContext.traceId();
            } catch (Exception ignored) {
            }
            traceId = StringUtils.isNotBlank(traceId) ? traceId : UUID.randomUUID().toString().replace("-", "");

            AppContext.set("LOG_UID", traceId);

            // 接收到请求，记录请求内容
            time.set(System.currentTimeMillis());
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            assert attributes != null;
            HttpServletRequest request = attributes.getRequest();
            String url = getPath(request.getRequestURL().toString());
            AppContext.set("REQUEST_URL", url);

            // 记录下请求内容
            log.info("monitor_log||请求||开始||{}||-||{}",
                    url,
                    Arrays.toString(joinPoint.getArgs()));
        } catch (Exception e) {
            log.info("日志打印错误");
        }
    }

}
