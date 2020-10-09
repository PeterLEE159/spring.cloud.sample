package com.cloud.service.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.cloud.service.user.vo.UserVO;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class SecurityFilter extends UsernamePasswordAuthenticationFilter {	
	
	/**
	 * login request api 요청 시
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		try {
			
			UserVO user = new ObjectMapper().readValue(request.getInputStream(), UserVO.class);
			
			return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
					user.getEmail(),
					user.getPassword(),
					new ArrayList()
				)
			);
			
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.attemptAuthentication(request, response);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
	
		String username = ((User)auth.getPrincipal()).getUsername();
		
		// db조회 필요
		UserVO user = new UserVO();
		user.setUsername(username);
		
		String token = Jwts.builder()
				.setSubject(user.getUsername())
				.setExpiration(new Date())
				.signWith(SignatureAlgorithm.HS256, "SOME SALT")
				.compact();
		response.addHeader("token", token);
	}
}
