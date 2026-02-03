package com.easy_plan._03_domain.auth.model;

import java.util.Objects;

import lombok.Getter;

@Getter
public class TokenClaims {
	private final Long userId;
	
	private final String role;
	
	private final Jti jti;
	
	private final TokenExpiration expiration;
	
	public TokenClaims(Long userId, String role, Jti jti, TokenExpiration expiration) {
		this.userId = userId;
		this.role = role;
		this.jti = jti;
		this.expiration = expiration;
	}
	
	public AccessToken toAccessToken(String value) {
		return new AccessToken(value, jti, expiration);
	}

	@Override
	public int hashCode() {
		return Objects.hash(jti, role, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		TokenClaims other = (TokenClaims) obj;
		return Objects.equals(jti, other.jti) && Objects.equals(role, other.role) && Objects.equals(userId, other.userId);
	}
}
