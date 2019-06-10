package com.san.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesHelperService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${server.port}")
	public int port;
}
