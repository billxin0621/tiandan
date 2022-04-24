//package com.sinosoft.fragins.framework.mq;
//
//import java.util.List;
//import java.util.UUID;
//
//import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
//import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
//import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
//import org.apache.rocketmq.common.message.MessageExt;
//import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.core.annotation.Order;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Order(Integer.MAX_VALUE)
//public class RocketMQConsumer implements InitializingBean, ApplicationContextAware {
//
//	private String namesrvAddr;
//
//	private String consumerGroup;
//
//	private ApplicationContext ctx;
//
//	@Override
//	public void afterPropertiesSet() throws Exception {
//		List<MqListenerInfo> listeners = MqListenerScanner.scanForListeners(ctx);
//		for (MqListenerInfo listener : listeners) {
//			try {
//				String consumerGroupName = consumerGroup + "_" + listener.getTopic();
//				if (!"*".equals(listener.getTags())) {
//					consumerGroupName += "_" + listener.getTags().replace("|", "_").replaceAll("\\s", "_");
//				}
//				DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroupName);
//				consumer.setInstanceName(UUID.randomUUID().toString());
//				consumer.setNamesrvAddr(namesrvAddr);
//				if (listener.isBroadcast()) {
//					consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
//					consumer.setMessageModel(MessageModel.BROADCASTING);
//				}
//				consumer.setConsumeThreadMax(128); // 消息接收最大128线程
//				consumer.setMaxReconsumeTimes(16); // 消息最多重试16次
//				consumer.subscribe(listener.getTopic(), listener.getTags());
//				consumer.registerMessageListener(new MessageListenerConcurrently() {
//					@Override
//					public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
//							ConsumeConcurrentlyContext context) {
//						try {
//							for (MessageExt msg : msgs) {
//								byte[] data = msg.getBody();
//								listener.handleMessage(data);
//							}
//						} catch (Exception e) {
//							log.error("Consume Message Exception", e);
//							return ConsumeConcurrentlyStatus.RECONSUME_LATER;
//						}
//						return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//					}
//				});
//				consumer.start();
//			} catch (Exception e) {
//				log.error("Error subscribe RocketMQ listener: topic={}, tags={}", listener.getTopic(),
//						listener.getTags(), e);
//			}
//		}
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
//	public String getConsumerGroup() {
//		return consumerGroup;
//	}
//
//	public void setConsumerGroup(String consumerGroup) {
//		this.consumerGroup = consumerGroup;
//	}
//
//	@Override
//	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//		this.ctx = applicationContext;
//	}
//
//}
