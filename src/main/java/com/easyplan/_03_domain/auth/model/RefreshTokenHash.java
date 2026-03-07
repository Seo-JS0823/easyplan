package com.easyplan._03_domain.auth.model;

import java.util.regex.Pattern;

import com.easyplan._03_domain.auth.exception.AuthException;
import com.easyplan._03_domain.auth.exception.AuthValueError;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class RefreshTokenHash {
	private static final Pattern PATTERN = Pattern.compile("^[A-Za-z0-9+/]+=*$");
	
	private final String value;
	
	private RefreshTokenHash(String value) {
		this.value = value;
	}
	
	public static RefreshTokenHash of(String value) {
		if(value == null || value.isBlank()) {
			throw new AuthException(AuthValueError.INVALID_REFRESH_TOKEN_HASH);
		}
		
		if(!PATTERN.matcher(value).matches()) {			
			throw new AuthException(AuthValueError.INVALID_REFRESH_TOKEN_HASH);
		}
		
		return new RefreshTokenHash(value);
	}
}
