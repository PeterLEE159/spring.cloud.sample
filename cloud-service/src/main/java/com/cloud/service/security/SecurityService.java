package com.cloud.service.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cloud.service.user.vo.UserVO;

@Service
public class SecurityService implements UserDetailsService {

	Map<String, UserVO> userMap = new HashMap<String, UserVO>();
	
	@Autowired
	BCryptPasswordEncoder encoder;

	@PostConstruct
	public void init() {

		UserVO u1 = new UserVO();
		u1.setUsername("lgw888@naver.com");
		u1.setPassword("password1");
		u1.setEncodedPassword(this.encoder.encode(u1.getPassword()));

		UserVO u2 = new UserVO();
		u2.setUsername("peterlee13795@gmail.com");
		u2.setPassword("password2");
		u2.setEncodedPassword(this.encoder.encode(u2.getPassword()));

		this.userMap.put("lgw888@naver.com", u1);
		this.userMap.put("peterlee13795@gmail.com", u2);
	}
	
	public UserVO create(UserVO user) {

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		user.setEncodedPassword(this.encoder.encode(user.getPassword()));
		this.userMap.put(user.getUsername(), user);

		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (!this.userMap.containsKey(username))
			throw new UsernameNotFoundException("user not found ! ");
		
		UserVO user = this.userMap.get(username);
		User userDetail = new User(user.getUsername(), user.getEncodedPassword(), true, true, true, true,
				new ArrayList());
		return userDetail;
	}

}
