package com.cloud.service.security;

import java.util.ArrayList;

import org.springframework.security.config.annotation.authentication.configurers.userdetails.UserDetailsServiceConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cloud.service.user.vo.UserVO;

@Service
public class SecurityService implements UserDetailsService {
	
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserVO user = new UserVO();
		user.setUsername(username);
		if(user == null) throw new UsernameNotFoundException("user not found ! ");
		return new User(user.getUsername(), user.getPassword(), true, true, true, true, new ArrayList());
	}
	
}
