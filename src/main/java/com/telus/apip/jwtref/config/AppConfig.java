package com.telus.apip.jwtref.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.telus.apip.jwtref.security.JWTTokenFilter;
import com.telus.apip.jwtref.security.JWTTokenProvider;

@Configuration
public class AppConfig {
	
	@Autowired
	JWTTokenProvider jwtTokenProvider;

	@Bean
	public FilterRegistrationBean<JWTTokenFilter> filterRegistrationBean() {
		FilterRegistrationBean<JWTTokenFilter> registrationBean = new FilterRegistrationBean<JWTTokenFilter>();
		//Will not autowire the JWTTokenProvider as it's being manually instantiated
		JWTTokenFilter jwtTokenFilter = new JWTTokenFilter(jwtTokenProvider);

		registrationBean.setFilter(jwtTokenFilter);
		registrationBean.addUrlPatterns("/mysecureapi2/*");
		registrationBean.setOrder(1); // set precedence
		return registrationBean;
	}

}
