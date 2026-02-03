package com.easy_plan._03_domain.auth.model;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Auth {
	private final Long authId;
	
	private final Long userId;
	
	private Jti jti;
	
	private TokenHash tokenHash;
	
	private TokenExpiration expiresAt;
	
	private final Instant createdAt;
	
	public static Auth from(Long userId, RefreshToken refreshToken, Instant now) {
		return Auth.builder()
				.userId(userId)
				.jti(refreshToken.getJti())
				.tokenHash(refreshToken.getTokenHash())
				.expiresAt(refreshToken.getExpiration())
				.createdAt(now)
				.build();
	}
	
	public void rotationRefreshToken(RefreshToken newRefreshToken) {
		this.jti = newRefreshToken.getJti();
		this.tokenHash = newRefreshToken.getTokenHash();
		this.expiresAt = newRefreshToken.getExpiration();
	}
	
	public boolean isExpired(Instant now) {
		return expiresAt.isExpired(now);
	}
	
	public boolean isValid(Instant now) {
		return !isExpired(now);
	}
}
