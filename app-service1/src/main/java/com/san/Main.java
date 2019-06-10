package com.san;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.netflix.discovery.EurekaClient;
import com.san.util.CommonUtil;

@SpringBootApplication
@EnableDiscoveryClient
public class Main {
	
	@Autowired
    @Lazy
    private EurekaClient eurekaClient;

	private static Logger logger = LoggerFactory.getLogger(Main.class);
	public static volatile long lastAccessedAt = System.currentTimeMillis();

	public static void main(String[] args) {
		logger.info("Starting application ... ");
		ConfigurableApplicationContext appContext = SpringApplication.run(Main.class, args);
		CommonUtil.ctx = appContext;
		logger.info("Application started ... ");
	}

	@Lazy
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		// WebMvcConfigurerAdapter, WebMvcConfigurer
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE").allowedOrigins("*").allowedHeaders("*");
			}
		};
	}
}
