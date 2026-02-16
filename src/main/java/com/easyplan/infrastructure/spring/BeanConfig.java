package com.easyplan.infrastructure.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.easyplan.common.time.Clock;
import com.easyplan.domain.auth.provider.TokenProvider;
import com.easyplan.domain.auth.repository.AuthSessionRepository;
import com.easyplan.domain.auth.repository.BlacklistRepository;
import com.easyplan.domain.auth.service.AuthSessionService;
import com.easyplan.domain.user.repository.PasswordHasher;
import com.easyplan.domain.user.repository.UserRepository;
import com.easyplan.domain.user.service.UserService;

@Configuration
public class BeanConfig {
	@Bean
	UserService userService(UserRepository userRepository, PasswordHasher passwordHasher, Clock clock) {
		return new UserService(userRepository, passwordHasher, clock);
	}
	
	@Bean
	AuthSessionService authSessionService(AuthSessionRepository authSessionRepository,
			BlacklistRepository blacklistRepository,
			TokenProvider tokenProvider) {
		return new AuthSessionService(authSessionRepository, blacklistRepository, tokenProvider);
	}
}
