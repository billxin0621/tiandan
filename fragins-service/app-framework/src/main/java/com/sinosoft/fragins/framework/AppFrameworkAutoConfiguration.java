package com.sinosoft.fragins.framework;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.sinosoft.fragins.framework.exception.ExceptionLoggerService;
import com.sinosoft.fragins.framework.mq.FakeMqClient;
import com.sinosoft.fragins.framework.mq.MqClient;
import com.sinosoft.fragins.framework.mq.MqCompensateService;
import com.sinosoft.fragins.framework.service.CommonJdbcService;
import com.sinosoft.fragins.framework.utils.OssFileStorage;
import com.sinosoft.fragins.framework.utils.RedisUtils;
import com.sinosoft.fragins.framework.utils.S3FileStorage;
import com.sinosoft.fragins.framework.utils.SpringContextUtils;
import com.sinosoft.fragins.framework.utils.VfsFileStorage;

@Configuration
@EnableCaching
@EnableScheduling
@EnableAspectJAutoProxy
@RibbonClients(defaultConfiguration = DefaultRibbonConfiguration.class)
public class AppFrameworkAutoConfiguration {

	@Bean
	public SpringContextUtils springContextUtils() {
		return new SpringContextUtils();
	}

	// ------ jdbc 组件注册 ------
	@Bean
	public CommonJdbcService commonJdbcService(@Autowired DataSource ds) {
		CommonJdbcService service = new CommonJdbcService();
		service.setDataSource(ds);
		return service;
	}

	@Bean
	public ExceptionLoggerService exceptionLoggerService(@Autowired DataSource ds) {
		ExceptionLoggerService service = new ExceptionLoggerService();
		service.setDataSource(ds);
		return service;
	}

	// ------ redis 组件注册 ------
	@Bean
	public RedisTemplate<String, Object> redisTemplate(@Autowired RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new JdkSerializationRedisSerializer());
		return template;
	}

	@Bean
	public RedisUtils redisUtils() {
		return new RedisUtils();
	}


	@Bean
	@ConditionalOnMissingBean
	public MqClient fakeMqClient() {
		return new FakeMqClient();
	}

	@Bean
	public MqCompensateService mqCompensateService(@Autowired DataSource ds) {
		MqCompensateService service = new MqCompensateService();
		service.setDataSource(ds);
		return service;
	}

	// ------ fileStorage 组件注册 ------
	@Bean
	@ConditionalOnProperty(name = "file-storage.type", havingValue = "vfs")
	@ConfigurationProperties(prefix = "file-storage.vfs")
	public VfsFileStorage vfsFileStorage() {
		return new VfsFileStorage();
	}

	@Bean
	@ConditionalOnProperty(name = "file-storage.type", havingValue = "oss")
	@ConfigurationProperties(prefix = "file-storage.oss")
	public OssFileStorage ossFileStorage() {
		return new OssFileStorage();
	}

	@Bean
	@ConditionalOnProperty(name = "file-storage.type", havingValue = "s3")
	@ConfigurationProperties(prefix = "file-storage.s3")
	public S3FileStorage s3FileStorage() {
		return new S3FileStorage();
	}

}
