package com.sinosoft.fragins.framework.exception;

import com.sinosoft.fragins.framework.utils.SpringContextUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 记录异常信息到uti_exception_log表，静态工具类，实际调用{@link ExceptionLoggerService}
 * 
 * @author panyu
 *
 */
@Slf4j
public class ExceptionLogger {

	/**
	 * 异步记录异常
	 * 
	 * @param t            异常对象
	 * @param businessType 业务类型，查询用，由业务程序自己定义
	 */
	public static void logAsync(Throwable t, String businessType) {
		try {
			ExceptionLoggerService service = SpringContextUtils.getBean(ExceptionLoggerService.class);
			service.logAsync(t, businessType);
		} catch (Exception e) {
			log.warn("记录异常日志发生错误", e);
		}
	}

	/**
	 * 异步记录异常
	 * 
	 * @param t            异常对象
	 * @param businessType 业务类型，查询用，由业务程序自己定义
	 * @param businessNo   业务号，查询用
	 */
	public static void logAsync(Throwable t, String businessType, String businessNo) {
		try {
			ExceptionLoggerService service = SpringContextUtils.getBean(ExceptionLoggerService.class);
			service.logAsync(t, businessType, businessNo);
		} catch (Exception e) {
			log.warn("记录异常日志发生错误", e);
		}
	}

}
