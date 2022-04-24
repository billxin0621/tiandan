package com.sinosoft.fragins.framework.mq;

import lombok.extern.slf4j.Slf4j;

/**
 * 不会实际发送消息的MqClient，适用于部分演示环境没法连接Mq但不需要相关功能的情况
 * 
 * @author panyu
 *
 */
@Slf4j
public class FakeMqClient implements MqClient {

	@Override
	public void sendMessage(String topic, String tag, String message) {
		log.warn("未进行MQ配置，消息未发送：topic={}, tag={}, message={}", topic, tag, message);
	}

	@Override
	public void sendMessage(String topic, String tag, String message, String key) {
		log.warn("未进行MQ配置，消息未发送：topic={}, tag={}, message={}, key={}", topic, tag, message, key);
	}

	@Override
	public void sendMessage(String topic, String tag, String message, boolean broadcast) {
		log.warn("未进行MQ配置，消息未发送：topic={}, tag={}, message={}, broadcast={}", topic, tag, message, broadcast);
	}

	@Override
	public void sendMessage(String topic, String tag, byte[] message) {
		log.warn("未进行MQ配置，消息未发送：topic={}, tag={}, message={}", topic, tag, message);
	}

	@Override
	public void sendMessage(String topic, String tag, byte[] message, boolean broadcast) {
		log.warn("未进行MQ配置，消息未发送：topic={}, tag={}, message={}, broadcast={}", topic, tag, message, broadcast);
	}

}
