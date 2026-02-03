package com.easy_plan._05_shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.easy_plan._03_domain.auth.AuthRepository;
import com.easy_plan._03_domain.auth.AuthService;
import com.easy_plan._03_domain.auth.JwtTokenProvider;
import com.easy_plan._03_domain.auth.TokenBlacklistRepository;
import com.easy_plan._03_domain.user.UserRepository;
import com.easy_plan._03_domain.user.UserService;
import com.easy_plan._05_shared.Clock;

@Configuration
public class ServiceBeanConfig {
	
	@Bean
	UserService userService(UserRepository userRepository) {
		return new UserService(userRepository);
	}
	
	@Bean
	AuthService authService(AuthRepository authRepository,
			JwtTokenProvider jwtTokenProvider,
			TokenBlacklistRepository tokenBlacklistRepository,
			Clock clock) {
		return new AuthService(authRepository, jwtTokenProvider, tokenBlacklistRepository, clock);
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
