package com.easyplan._03_domain.auth.model;

import java.util.Objects;

import com.easyplan.shared.util.Require;

import lombok.Getter;

@Getter
public class TokenClaims {
	private final Subject subject;
	
	private final TokenId tokenId;
	
	private final String role;
	
	private final TokenExpiration expiresAt;
	
	public TokenClaims(Subject subject, TokenId tokenId, String role, TokenExpiration expiresAt) {
		this.subject = Require.require(subject, "TokenClaims:subject");
		this.tokenId = Require.require(tokenId, "TokenClaims:tokenId");
		this.expiresAt = Require.require(expiresAt, "TokenClaims:expiresAt");
		this.role = Require.require(role, "TokenClaims:role");
	}

	@Override
	public int hashCode() {
		return Objects.hash(role, subject, tokenId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TokenClaims other = (TokenClaims) obj;
		return Objects.equals(role, other.role) && Objects.equals(subject, other.subject)
				&& Objects.equals(tokenId, other.tokenId);
	}
	
}
