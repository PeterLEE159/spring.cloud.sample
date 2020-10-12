package com.cloud.zuul.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	Environment env;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
		http.csrf().disable();
		http.headers().frameOptions().disable();
		
		http.authorizeRequests()
			.antMatchers(this.env.getProperty("uri.actuator.path")).permitAll()
			.antMatchers(this.env.getProperty("uri.zuul.h2console.path")).permitAll()
			.antMatchers(HttpMethod.POST, this.env.getProperty("uri.zuul.user.signup")).permitAll()
			.antMatchers(HttpMethod.POST, this.env.getProperty("uri.zuul.user.signin")).permitAll()
			.anyRequest().authenticated()
		.and()
			.addFilter(new SecurityFilter(this.authenticationManager(), env));
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	

}
