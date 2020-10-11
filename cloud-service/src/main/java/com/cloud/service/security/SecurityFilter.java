package com.cloud.service.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cloud.service.user.vo.UserVO;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class SecurityFilter extends UsernamePasswordAuthenticationFilter {

	BCryptPasswordEncoder encoder;
	
	Environment env;

	public SecurityFilter(BCryptPasswordEncoder encoder, Environment env) {
		this.encoder = encoder;
		this.env = env;
	}

	{
		this.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {

			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
				exception.printStackTrace();

			}
		});
	}

	/**
	 * login request api 요청 시
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {

			UserVO user = new ObjectMapper().readValue(request.getInputStream(), UserVO.class);
			return this.getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), Arrays.asList()));

		} catch (IOException e) {
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
		Date expiredDate = new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("jwt.expiration")));
		
		String token = Jwts.builder()
				.setSubject(user.getUsername())
				.setExpiration(expiredDate)
				.signWith(SignatureAlgorithm.HS512, this.env.getProperty("jwt.secret"))
				.compact();
		response.addHeader("token", token);
	}
}
