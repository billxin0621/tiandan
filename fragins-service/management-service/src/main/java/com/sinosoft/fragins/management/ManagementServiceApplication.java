package com.sinosoft.fragins.management;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import tk.mybatis.mapper.common.Mapper;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.unit.DataSize;
import tk.mybatis.spring.annotation.MapperScan;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableFeignClients
@MapperScan("com.sinosoft.fragins.**.dao")
@ComponentScan(value = {"com.sinosoft.fragins.management","com.sinosoft.fragins.framework.config","com.sinosoft.fragins.code"})
@EnableApolloConfig
public class ManagementServiceApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ManagementServiceApplication.class).web(WebApplicationType.SERVLET).run(args);
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		//允许上传的文件最大值
		factory.setMaxFileSize(DataSize.parse("100MB"));
		/// 设置总上传数据总大小
		factory.setMaxRequestSize(DataSize.parse("100MB"));
		return factory.createMultipartConfig();
	}

}
