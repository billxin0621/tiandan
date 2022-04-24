package com.sinosoft.fragins.framework.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 提供获取ApplicationContext的静态方法
 */
public class SpringContextUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	public static <T> T getBean(Class<T> requiredType) {
		return applicationContext.getBean(requiredType);
	}

	public static String getEnvironmentType() {
		return getProperty("application.environmentType");
	}

	public static String getProperty(String key) {
		return applicationContext.getEnvironment().getProperty(key);
	}

	public static String getProperty(String key, String defaultValue) {
		return applicationContext.getEnvironment().getProperty(key, defaultValue);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextUtils.applicationContext = applicationContext;
	}

}
