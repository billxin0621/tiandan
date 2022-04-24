package com.sinosoft.fragins.framework.mq;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.scheduling.annotation.Scheduled;

import com.sinosoft.fragins.framework.utils.RedisUtils;
import com.sinosoft.fragins.framework.utils.RedisUtils.RedisLock;
import com.sinosoft.fragins.framework.utils.SpringContextUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * mq发送失败自动补偿
 * 
 * @author panyu
 *
 */
@Slf4j
public class MqCompensateService extends JdbcDaoSupport {

	@Value("${spring.application.name}")
	private String appName;

	public void saveMqCompensate(String topic, String tag, String content) {
		String sql = "insert into uti_mq_compensate (send_time, topic, tag, content, resend_result) values (?, ?, ?, ?, ?)";
		getJdbcTemplate().update(sql, new Date(), topic, tag, content, "0");
	}

	public void saveMqCompensate(String topic, String tag, String content, Date sendDate) {
		String sql = "insert into uti_mq_compensate (send_time, topic, tag, content, resend_result) values (?, ?, ?, ?, ?)";
		getJdbcTemplate().update(sql, sendDate, topic, tag, content, "0");
	}

	@Scheduled(cron = "0 * * * * ?")
	public void mqCompensate() {
		if (!"on".equalsIgnoreCase((String) RedisUtils.get("MqCompensateSwitch"))) {
			return;
		}
		RedisLock lock = RedisUtils.lockNoWait("MqCompensateTask." + appName, 30000);
		if (lock != null) {
			try {
				MqClient client = SpringContextUtils.getBean(MqClient.class);
				Date processDate = new Date(System.currentTimeMillis() - 86400000);
				String sql = "select id, topic, tag, content from uti_mq_compensate where send_time > ? and send_time <= now() and resend_result in ('0','2') limit 5000";
				getJdbcTemplate().query(sql, new Object[] { processDate }, new RowCallbackHandler() {
					@Override
					public void processRow(ResultSet rs) throws SQLException {
						Long id = rs.getLong("id");
						String topic = rs.getString("topic");
						String tag = rs.getString("tag");
						String content = rs.getString("content");
						String sql = null;
						try {
							log.info("MQ补偿：topic={}, tag={}, content={}", topic, tag, content);
							client.sendMessage(topic, tag, content);
							sql = "update uti_mq_compensate set resend_result='1', resend_time=? where id=?";
							getJdbcTemplate().update(sql, new Date(), id);
						} catch (Exception e) {
							log.error("MQ补偿失败", e);
							sql = "update uti_mq_compensate set resend_result='2', resend_time=? where id=?";
							getJdbcTemplate().update(sql, new Date(), id);
						}
					}
				});
			} finally {
				lock.unlock();
			}
		}
	}

	@Data
	public static class MqCompensate {
		private Long id;
		private String topic;
		private String tag;
		private String content;
	}

}
