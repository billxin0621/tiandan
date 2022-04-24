//package com.sinosoft.fragins.framework.mq;
//
//import java.util.UUID;
//
//import org.apache.rocketmq.client.producer.DefaultMQProducer;
//import org.apache.rocketmq.common.message.Message;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class RocketMQClient extends MqClientSupport implements InitializingBean {
//
//	private String namesrvAddr;
//
//	private String producerGroup;
//
//	private DefaultMQProducer producer;
//
//	@Autowired
//	private MqCompensateService mqCompensateService;
//
//	@Override
//	public void sendMessageInternal(String topic, String tag, byte[] message, boolean broadcast) throws Exception {
//		try {
//			Message msg = new Message(topic, tag, message);
//			producer.send(msg);
//		} catch (Exception e) {
//			if (mqCompensateService == null) {
//				log.error("MQ发送失败");
//				throw e;
//			} else {
//				log.error("MQ发送失败，插入补偿表稍后重试", e);
//				mqCompensateService.saveMqCompensate(topic, tag, new String(message, "UTF-8"));
//			}
//		}
//	}
//
//	@Override
//	public void sendMessageInternalWithKey(String topic, String tag, byte[] message, boolean broadcast,String keys) throws Exception {
//		try {
//			Message msg = new Message(topic, tag, keys, message);
//			producer.send(msg);
//		} catch (Exception e) {
//			if (mqCompensateService == null) {
//				log.error("MQ发送失败");
//				throw e;
//			} else {
//				log.error("MQ发送失败，插入补偿表稍后重试", e);
//				mqCompensateService.saveMqCompensate(topic, tag, new String(message, "UTF-8"));
//			}
//		}
//	}
//
//	@Override
//	public void afterPropertiesSet() throws Exception {
//		producer = new DefaultMQProducer(producerGroup);
//		producer.setInstanceName(UUID.randomUUID().toString());
//		producer.setNamesrvAddr(namesrvAddr);
//		producer.setSendMsgTimeout(10000);
//		producer.start();
//	}
//
//	public String getNamesrvAddr() {
//		return namesrvAddr;
//	}
//
//	public void setNamesrvAddr(String namesrvAddr) {
//		this.namesrvAddr = namesrvAddr;
//	}
//
//	public String getProducerGroup() {
//		return producerGroup;
//	}
//
//	public void setProducerGroup(String producerGroup) {
//		this.producerGroup = producerGroup;
//	}
//
//}
