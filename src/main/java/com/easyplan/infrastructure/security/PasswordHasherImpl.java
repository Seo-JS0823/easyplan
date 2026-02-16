package com.easyplan.infrastructure.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.easyplan.domain.user.model.Password;
import com.easyplan.domain.user.model.PasswordHash;
import com.easyplan.domain.user.repository.PasswordHasher;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PasswordHasherImpl implements PasswordHasher {
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public PasswordHash encode(Password password) {
		String encoded = passwordEncoder.encode(password.getValue());
		
		PasswordHash result = PasswordHash.of(encoded);
		
		return result;
	}

	// 해시 검증 통과 = true
	@Override
	public boolean matches(Password raw, PasswordHash encoded) {
		String rawPassword = raw.getValue();
		String encodedPassword = encoded.getHashedValue();
		
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}
	
}
