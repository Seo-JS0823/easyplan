package com.easyplan._04_infra.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.easyplan._03_domain.auth.repository.AuthRepository;
import com.easyplan._03_domain.auth.repository.BlacklistRepository;
import com.easyplan._03_domain.auth.service.AuthService;
import com.easyplan._03_domain.auth.service.TokenService;
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
	
	@Bean
	AuthService authService(AuthRepository authRepo, BlacklistRepository blacklistRepo, TokenService tokenService, Clock clock) {
		return new AuthService(authRepo, blacklistRepo, tokenService, clock);
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
