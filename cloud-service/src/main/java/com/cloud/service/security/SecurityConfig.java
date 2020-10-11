package com.cloud.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	SecurityService securityService;
	
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Autowired
	Environment env;
	
	@Value("${ip.loadBalancer}")
	String loadBalancerIP;
	
	@Value("${uri.user.signin}")
	String signinURI;

	
	@Bean
	public BCryptPasswordEncoder initEncoderBeanObject() {
	  return new BCryptPasswordEncoder();
	}
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.headers().frameOptions().disable();

		http.authorizeRequests()
			.antMatchers("/**")
			.hasIpAddress(this.loadBalancerIP)
			.and()
			.addFilter(this.getAuthenticationFilter());
		
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.securityService).passwordEncoder(this.encoder);
	}
	
	private SecurityFilter getAuthenticationFilter() throws Exception {
		SecurityFilter filter = new SecurityFilter(this.encoder, this.env);
		filter.setAuthenticationManager(this.authenticationManager());
		filter.setFilterProcessesUrl(this.signinURI);
		return filter;
	}
	
	
}
