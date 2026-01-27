package com.easy_plan._05_shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.easy_plan._03_domain.user.UserRepository;
import com.easy_plan._03_domain.user.UserService;

@Configuration
public class ServiceBeanConfig {
	
	@Bean
	UserService userService(UserRepository userRepository) {
		return new UserService(userRepository);
	}
}
