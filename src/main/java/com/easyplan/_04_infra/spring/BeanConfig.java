package com.easyplan._04_infra.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.easyplan._03_domain.user.repository.UserRepository;
import com.easyplan._03_domain.user.service.PasswordService;
import com.easyplan._03_domain.user.service.UserService;
import com.easyplan.shared.time.Clock;

@Configuration
public class BeanConfig {
	
	@Bean
	UserService userService(UserRepository userRepo, PasswordService passwordService, Clock clock) {
		return new UserService(userRepo, passwordService, clock);
	}
}
