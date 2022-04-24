package com.sinosoft.fragins.framework.mq;

import lombok.extern.slf4j.Slf4j;
import com.sinosoft.fragins.framework.config.AppContext;

@Slf4j
public abstract class MqClientSupport implements MqClient {

	/** 实际发送MQ的方法 */
	protected abstract void sendMessageInternal(String topic, String tag, byte[] message, boolean broadcast)
			throws Exception;

	//新增有key发送 begin
	protected abstract void sendMessageInternalWithKey(String topic, String tag, byte[] message, boolean broadcast,String key)
			throws Exception;
	@Override
	public void sendMessage(String topic, String tag, String message,String keys) {
		try {
			Boolean isPressureTest = AppContext.get("isPressureTest");
			if (isPressureTest == null || !isPressureTest) {
				this.sendMessageInternalWithKey(topic, tag, message.getBytes("UTF-8"), false, keys);
				log.debug("发送MQ成功: topic={}, tag={}, message={}, keys={}", topic, tag, message, keys);
			}
		} catch (Exception e) {
			log.error("发送MQ异常: topic={}, tag={}, message={}, keys={}", topic, tag, message, keys);
			throw new RuntimeException(e);
		}
	}
	//新增有key发送 end

	@Override
	public void sendMessage(String topic, String tag, String message) {
		try {
			Boolean isPressureTest = AppContext.get("isPressureTest");
			if (isPressureTest == null || !isPressureTest) {
				this.sendMessageInternal(topic, tag, message.getBytes("UTF-8"), false);
				log.debug("发送MQ成功: topic={}, tag={}, message={}", topic, tag, message);
			}
		} catch (Exception e) {
			log.error("发送MQ异常: topic={}, tag={}, message={}", topic, tag, message);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void sendMessage(String topic, String tag, String message, boolean broadcast) {
		try {
			Boolean isPressureTest = AppContext.get("isPressureTest");
			if (isPressureTest == null || !isPressureTest) {
				this.sendMessageInternal(topic, tag, message.getBytes("UTF-8"), broadcast);
				log.debug("发送MQ成功: topic={}, tag={}, message={}, broadcast={}", topic, tag, message, broadcast);
			}
		} catch (Exception e) {
			log.error("发送MQ异常: topic={}, tag={}, message={}, broadcast={}", topic, tag, message, broadcast);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void sendMessage(String topic, String tag, byte[] message) {
		try {
			Boolean isPressureTest = AppContext.get("isPressureTest");
			if (isPressureTest == null || !isPressureTest) {
				this.sendMessageInternal(topic, tag, message, false);
				log.debug("发送MQ成功: topic={}, tag={}, message={}", topic, tag, message);
			}
		} catch (Exception e) {
			log.error("发送MQ异常: topic={}, tag={}, message={}", topic, tag, message);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void sendMessage(String topic, String tag, byte[] message, boolean broadcast) {
		try {
			Boolean isPressureTest = AppContext.get("isPressureTest");
			if (isPressureTest == null || !isPressureTest) {
				this.sendMessageInternal(topic, tag, message, broadcast);
				log.debug("发送MQ成功: topic={}, tag={}, message={}, broadcast={}", topic, tag, message, broadcast);
			}
		} catch (Exception e) {
			log.error("发送MQ异常: topic={}, tag={}, message={}, broadcast={}", topic, tag, message, broadcast);
			throw new RuntimeException(e);
		}
	}

}
