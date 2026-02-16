package com.easyplan.domain.auth.model;

import java.time.Instant;
import java.util.regex.Pattern;

import com.easyplan.domain.auth.error.AuthSessionErrorCode;
import com.easyplan.domain.auth.error.AuthSessionException;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public final class AccessToken {
	private static final Pattern JWT_PATTERN = Pattern.compile("^[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+$");
	
	private static final int JWT_MIN_LENGTH = 50;
	
	private final String value;
	
	private final TokenId tokenId;
	
	private final Instant expiresAt;
	
	private AccessToken(String value, TokenId tokenId, Instant expiresAt) {
		this.value = value;
		this.tokenId = tokenId;
		this.expiresAt = expiresAt;
	}
	
	public static AccessToken of(String value, TokenId tokenId, Instant expiresAt) {
		if(value == null || value.isBlank()) {
			throw new AuthSessionException(AuthSessionErrorCode.INVALID_ACCESS_TOKEN);
		}
		
		if(value.length() < JWT_MIN_LENGTH && !JWT_PATTERN.matcher(value).matches()) {
			throw new AuthSessionException(AuthSessionErrorCode.INVALID_ACCESS_TOKEN_UNSUPPORTED);
		}
		
		return new AccessToken(value, tokenId, expiresAt);
	}
}
