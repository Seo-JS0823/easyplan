package com.easyplan.domain.auth.model;

import java.util.regex.Pattern;

import com.easyplan.domain.auth.error.AuthSessionErrorCode;
import com.easyplan.domain.auth.error.AuthSessionException;

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
		if(value == null || value.isBlank() && !PATTERN.matcher(value).matches()) {
			throw new AuthSessionException(AuthSessionErrorCode.INVALID_TOKEN_HASH);
		}
		
		return new RefreshTokenHash(value);
	}
}
