package com.easyplan._03_domain.auth.model;

import java.time.Instant;

import com.easyplan._03_domain.auth.exception.AuthException;
import com.easyplan._03_domain.auth.exception.AuthValueError;

import lombok.Getter;

@Getter
public class TokenExpiration {
	private final Instant value;
	
	private TokenExpiration(Instant value) {
		this.value = value;
	}
	
	public static TokenExpiration of(Instant value) {
		if(value == null) {
			throw new AuthException(AuthValueError.INVALID_EXPIRATION);
		}
		
		return new TokenExpiration(value);
	}
	
	public boolean isExpired(Instant now) {
		return value.isBefore(now);
	}
}
