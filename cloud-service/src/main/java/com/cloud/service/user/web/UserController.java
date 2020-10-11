package com.cloud.service.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Value("${eureka.instance.instanceId}")
	String eurekaInstanceId;
	
	@Value("${jwt.secret}")
	String jwtSecret;
	
	@Autowired
	Environment env;
	
	@GetMapping(value="status", produces = "text/plain")
	public String getStatus() {
		return String.format("instance id : %s, %s, %s", this.eurekaInstanceId, this.jwtSecret, this.env.getProperty("jwt.secret"));
	}
}
