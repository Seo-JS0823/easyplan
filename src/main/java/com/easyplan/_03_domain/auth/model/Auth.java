package com.easyplan._03_domain.auth.model;

import java.time.Instant;

import lombok.Getter;

@Getter
public class Auth {
	private final Long id;
	
	private final Long userId;
	
	private RefreshTokenHash refreshTokenHash;
	
	private TokenExpiration expiresAt;
	
	private final Instant createdAt;
	
	private Instant updatedAt;
	
	Auth(Long id, Long userId, RefreshTokenHash refreshTokenHash, TokenExpiration expiresAt, Instant createdAt, Instant updatedAt) {
		this.id = id;
		this.userId = userId;
		this.refreshTokenHash = refreshTokenHash;
		this.expiresAt = expiresAt;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}
