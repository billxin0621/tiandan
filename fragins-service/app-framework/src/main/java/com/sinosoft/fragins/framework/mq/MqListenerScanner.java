package com.sinosoft.fragins.framework.mq;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MqListenerScanner {

	public static List<MqListenerInfo> scanForListeners(ApplicationContext ctx) {
		try {
			List<MqListenerInfo> list = new ArrayList<MqListenerInfo>();
			String[] names = ctx.getBeanDefinitionNames();
			log.info("Scanning {} beans for MqListener...", names.length);
			for (String name : names) {
				Object bean = ctx.getBean(name);
				Class<?> beanClass = bean.getClass();
				if (AopUtils.isAopProxy(bean)) {
					beanClass = AopUtils.getTargetClass(bean);
				}
				Method[] methods = beanClass.getDeclaredMethods();
				for (Method method : methods) {
					MqListener annotation = method.getAnnotation(MqListener.class);
					if (annotation != null) {
						if (method.getParameterCount() != 1) {
							log.error("{}.{} has multiple parameters, cannot be registered as MqListener...",
									beanClass.getName(), method.getName());
							continue;
						}
						if (!method.getParameterTypes()[0].equals(String.class)
								&& !method.getParameterTypes()[0].isArray()) { // TODO 暂时不知道好的byte[]检测方法
							log.error("{}.{} parameter is not String or byte[], cannot be registered as MqListener...");
							continue;
						}
						MqListenerInfo listenerInfo = new MqListenerInfo(bean, annotation, method);
						list.add(listenerInfo);
						log.info("Registered {}.{} as MqListener.", beanClass.getName(), method.getName());
					}
				}
			}
			log.info("Finished scan for MqListener.");
			return list;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
