package com.easyplan._03_domain.auth.model;

import java.util.UUID;

import com.easyplan._03_domain.auth.exception.AuthException;
import com.easyplan._03_domain.auth.exception.AuthValueError;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class TokenId {
	private final String value;
	
	private TokenId(String value) {
		this.value = value;
	}
	
	public static TokenId of(String value) {
		if(value == null || value.isBlank()) {
			throw new AuthException(AuthValueError.INVALID_JWT);
		}
		
		try {
			UUID.fromString(value);
		} catch (IllegalArgumentException e) {
			throw new AuthException(AuthValueError.INVALID_JWT);
		}
		
		return new TokenId(value);
	}
	
	public static TokenId create() {
		return new TokenId(UUID.randomUUID().toString());
	}
}
