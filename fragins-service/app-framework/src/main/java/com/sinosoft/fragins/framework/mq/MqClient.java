package com.sinosoft.fragins.framework.mq;

public interface MqClient {

	/**
	 * 以非广播模式发送MQ消息
	 * 
	 * @param topic   主题或队列名称
	 * @param tag     标签信息
	 * @param message 消息体
	 */
	public void sendMessage(String topic, String tag, String message);

	/**
	 * 以非广播模式发送MQ消息
	 *
	 * @param topic   主题或队列名称
	 * @param tag     标签信息
	 * @param message 消息体
	 * @param key 	  key
	 */
	public void sendMessage(String topic, String tag, String message,String keys);

	/**
	 * 发送MQ消息
	 * 
	 * @param topic     主题或队列名称
	 * @param tag       标签信息
	 * @param message   消息体
	 * @param broadcast 是否广播方式
	 */
	public void sendMessage(String topic, String tag, String message, boolean broadcast);

	/**
	 * 以非广播模式发送MQ消息
	 * 
	 * @param topic   主题或队列名称
	 * @param tag     标签信息
	 * @param message 消息体
	 */
	public void sendMessage(String topic, String tag, byte[] message);

	/**
	 * 发送MQ消息
	 * 
	 * @param topic     主题或队列名称
	 * @param tag       标签信息
	 * @param message   消息体
	 * @param broadcast 是否广播方式
	 */
	public void sendMessage(String topic, String tag, byte[] message, boolean broadcast);

}
