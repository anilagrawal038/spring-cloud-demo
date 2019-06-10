package com.san;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ConfigurableApplicationContext;
import com.san.util.CommonUtil;

@SpringBootApplication
@EnableEurekaServer
public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		logger.info("Starting application ... ");
		ConfigurableApplicationContext appContext = SpringApplication.run(Main.class, args);
		CommonUtil.ctx = appContext;
		logger.info("Application started ... ");
	}
}
