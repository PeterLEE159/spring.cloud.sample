package com.cloud.zuul.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	
	@Value("${uri-zuul.user.signin}")
	private String signinURI;
	
	@Value("${uri-zuul.user.signup}")
	private String signupURI;
	
	@Value("${uri-zuul.h2console.path}")
	private String h2ConsoleURI;
	
	@Autowired
	Environment env;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
		http.csrf().disable();
		http.headers().frameOptions().disable();
		
		http.authorizeRequests()
				.antMatchers(this.h2ConsoleURI).permitAll()
				.antMatchers(HttpMethod.POST, this.signupURI).permitAll()
				.antMatchers(HttpMethod.POST, this.signinURI).permitAll()
				.anyRequest().authenticated()
			.and()
				.addFilter(new SecurityFilter(this.authenticationManager(), env));
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	

}
