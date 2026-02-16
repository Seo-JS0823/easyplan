package com.easyplan.domain.auth.model;

import java.time.Instant;
import java.util.regex.Pattern;

import com.easyplan.domain.auth.error.AuthSessionErrorCode;
import com.easyplan.domain.auth.error.AuthSessionException;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public final class RefreshToken {
	private static final Pattern PATTERN = Pattern.compile("^[A-Za-z0-9]{64}$");
	
	private final String value;
	
	private final Instant expiresAt;
	
	private RefreshToken(String value, Instant expiresAt) {
		this.value = value;
		this.expiresAt = expiresAt;
	}
	
	public static RefreshToken of(String value, Instant expiresAt) {
		validate(value);
		return new RefreshToken(value, expiresAt);
	}
	
	/**
	 * 클라이언트에서 읽어온 리프레시 토큰 생성용인데
	 * expiresAt은 Null임.
	 * 
	 * @param value
	 * @return
	 */
	public static RefreshToken ofFromRequest(String value) {
		validate(value);
		return new RefreshToken(value, null);
	}
	
	private static void validate(String value) {
		if(value == null || value.isBlank()) {
			throw new AuthSessionException(AuthSessionErrorCode.INVALID_REFRESH_TOKEN);
		}
		
		if(!PATTERN.matcher(value).matches()) {
			throw new AuthSessionException(AuthSessionErrorCode.INVALID_REFRESH_TOKEN_UNSUPPORTED);
		}
	}
}
