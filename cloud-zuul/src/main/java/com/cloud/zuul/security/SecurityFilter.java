package com.cloud.zuul.security;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;

public class SecurityFilter extends BasicAuthenticationFilter {
	
	Environment env;
	
	public SecurityFilter(AuthenticationManager authenticationManager, Environment env) {
		super(authenticationManager);
		this.env = env;
		
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String authHeader = request.getHeader("Authorization");
		
		if(authHeader == null || !authHeader.startsWith("Bearer")) {
			chain.doFilter(request, response);
			return;
		}
		
		UsernamePasswordAuthenticationToken token = getAuthentication(request);
		SecurityContextHolder.getContext().setAuthentication(token);
		chain.doFilter(request, response);
	}
	
	
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if(authHeader == null || !authHeader.startsWith("Bearer")) {
			return null;
		}
		
		String token = authHeader.replace("Bearer", "");
		
		String userId = Jwts.parser()
						.setSigningKey(this.env.getProperty("jwt.secret"))
						.parseClaimsJws(token)
						.getBody()
						.getSubject();
		
		if(userId == null) return null;
		
		return new UsernamePasswordAuthenticationToken(userId, null, Arrays.asList());
	}

}
