package com.sinosoft.fragins.framework.mq;

import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class MqListenerInfo {

	private Object bean;
	private MqListener annotation;
	private Method method;
	private boolean messageAsString;

	public MqListenerInfo(Object bean, MqListener annotation, Method method) {
		this.bean = bean;
		this.annotation = annotation;
		this.method = method;
		messageAsString = method.getParameters()[0].getType().equals(String.class);
	}

	public Object getBean() {
		return this.bean;
	}

	public MqListener getAnnotation() {
		return this.annotation;
	}

	public Method getMethod() {
		return this.method;
	}

	public String getTopic() {
		return annotation.topic();
	}

	public String getTags() {
		return annotation.tags();
	}

	public boolean isBroadcast() {
		return annotation.broadcast();
	}

	public void handleMessage(byte[] message) throws Exception {
		if (messageAsString) {
			String text = new String(message, "UTF-8");
			log.debug("接收到mq消息：topic={}, tag={}, message={}", annotation.topic(), annotation.tags(), text);
			method.invoke(bean, text);
		} else {
			log.debug("接收到mq消息：topic={}, tag={}, message={}", annotation.topic(), annotation.tags(), message);
			method.invoke(bean, message);
		}
	}

}
