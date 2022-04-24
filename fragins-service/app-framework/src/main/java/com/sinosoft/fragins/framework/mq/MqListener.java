package com.sinosoft.fragins.framework.mq;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mq监听方法的标注，标注在方法上并指定监听的主题。
 * <p>
 * 注：标注所在的类必须是@Component声明过的Spring
 * Bean，标注的方法必须只有一个String或byte[]类型的入参，即一般是这样的方法签名：
 * <code>public void onSomeMessage(String message) throws Exception</code>或者
 * <code>public void onSomeRawMessage(byte[] message) throws Exception</code>
 * 
 * @author panyu
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MqListener {

	/**
	 * 监听的主题或队列名称
	 */
	public String topic();

	/**
	 * 监听的标签过滤，默认为全部监听
	 */
	public String tags() default "*";

	/**
	 * 监听的主题或队列是否是广播模式，默认为false
	 */
	public boolean broadcast() default false;

	/**
	 * 是否需要落异常表
	 */
	boolean exceptionLogger() default false;
}
