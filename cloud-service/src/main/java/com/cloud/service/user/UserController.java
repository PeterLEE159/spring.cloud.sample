package com.cloud.service.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Value("${eureka.instance.instanceId}")
	String eurekaInstanceId;
	
	@GetMapping(value="status", produces = "text/plain")
	public String getStatus() {
		return String.format("instance id : %s", this.eurekaInstanceId);
	}
}
