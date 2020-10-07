package com.cloud.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfigAdapter extends WebSecurityConfigurerAdapter {
	
	@Autowired
	SecurityService securityService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder; 
	
	@Bean
	public BCryptPasswordEncoder initEncoderBeanObject() {
	  return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		
		http.authorizeRequests()
			.antMatchers("/**")
			.hasIpAddress("192.168.0.4")
			.and()
			.addFilter(this.getAuthenticationFilter());
		
		http.headers().frameOptions().disable();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.securityService).passwordEncoder(this.passwordEncoder);
	}
	
	private SecurityFilter getAuthenticationFilter() throws Exception {
		SecurityFilter filter = new SecurityFilter();
		// filter.setAuthenticationManager(this.authenticationManager());
		filter.setFilterProcessesUrl("/users/login");
		return filter;
	}
	
	
}
