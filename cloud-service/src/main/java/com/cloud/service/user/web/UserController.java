package com.cloud.service.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Value("${eureka.instance.instanceId}")
	String eurekaInstanceId;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping(value="status", produces = "text/plain")
	public String getStatus() {
		System.out.println(bCryptPasswordEncoder.encode("password").toString());
		return String.format("instance id : %s", this.eurekaInstanceId);
	}
}
