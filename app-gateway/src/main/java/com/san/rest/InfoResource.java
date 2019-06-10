package com.san.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.san.co.CommonResponseCO;

@RestController
@RequestMapping("/gateway")
public class InfoResource {

	@Value("${user.role}")
	private String role;

	@RequestMapping(path = "/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public CommonResponseCO status() {
		CommonResponseCO response = new CommonResponseCO();
		response.setMessage("Everything is working fine. " + role);
		return response;
	}

}
