package com.easyplan.domain.auth.model;

import java.time.Instant;

public class AuthSessionFactory {
	private AuthSessionFactory() {}
	
	public static AuthSession create(
			Subject subject,
			RefreshTokenHash refreshTokenHash,
			Instant expiresAt,
			Instant createdAt) {
		
		if(expiresAt.isBefore(createdAt)) {
			// TODO: AuthException
		}
		
		return new AuthSession(subject, refreshTokenHash, expiresAt, createdAt);
	}
}
