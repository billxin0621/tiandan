package com.sinosoft.fragins.framework;

import org.springframework.cloud.netflix.ribbon.RibbonClientName;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.client.config.IClientConfig;

@Configuration(proxyBeanMethods = false)
public class DefaultRibbonConfiguration {
	
	@RibbonClientName
	private String name;
	
	@Bean
	public IClientConfig ribbonClientConfig() {
		DefaultClientConfigImpl config = new DefaultClientConfigImpl();
		config.loadProperties(this.name);
		config.set(CommonClientConfigKey.ConnectTimeout, 3000);
		config.set(CommonClientConfigKey.ReadTimeout, 30000);
		config.set(CommonClientConfigKey.GZipPayload, true);
		return config;
	}
	
}
