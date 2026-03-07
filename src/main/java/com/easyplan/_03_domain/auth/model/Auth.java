package com.easyplan._03_domain.auth.model;

import java.time.Instant;

import lombok.Getter;

@Getter
public class Auth {
	private final Long id;
	
	private final Long userId;
	
	private final Subject subject;
	
	private RefreshTokenHash refreshTokenHash;
	
	private TokenExpiration expiresAt;
	
	private final Instant createdAt;
	
	private Instant updatedAt;
	
	Auth(Long id, Long userId, Subject subject, RefreshTokenHash refreshTokenHash, TokenExpiration expiresAt, Instant createdAt, Instant updatedAt) {
		this.id = id;
		this.userId = userId;
		this.subject = subject;
		this.refreshTokenHash = refreshTokenHash;
		this.expiresAt = expiresAt;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
	public void updateTokenHash(RefreshTokenHash refreshTokenHash, Instant now) {
		this.refreshTokenHash = refreshTokenHash;
		this.updatedAt = now;
	}
}
