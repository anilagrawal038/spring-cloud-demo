package com.san;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.ConfigurableApplicationContext;
import com.san.util.CommonUtil;

@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		logger.info("Starting application ... ");
		ConfigurableApplicationContext appContext = SpringApplication.run(Main.class, args);
		CommonUtil.ctx = appContext;
		logger.info("Application started ... ");
	}
}
