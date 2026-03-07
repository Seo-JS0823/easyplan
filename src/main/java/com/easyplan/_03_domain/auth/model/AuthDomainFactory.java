package com.easyplan._03_domain.auth.model;

import java.time.Instant;

public class AuthDomainFactory {
	public static Auth create(
			Long userId,
			RefreshTokenHash refreshTokenHash,
			TokenExpiration expiresAt,
			Instant now
	) {
		return new Auth(null, userId, refreshTokenHash, expiresAt, now, now);
	}
	
	public static Auth read(
			Long id,
			Long userId,
			RefreshTokenHash refreshTokenHash,
			TokenExpiration expiresAt,
			Instant createdAt,
			Instant updatedAt
	) {
		return new Auth(id, userId, refreshTokenHash, expiresAt, createdAt, updatedAt);
	}
}
