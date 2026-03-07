package com.easyplan._03_domain.auth.model;

import java.util.Objects;
import java.util.regex.Pattern;

import com.easyplan._03_domain.auth.exception.AuthException;
import com.easyplan._03_domain.auth.exception.AuthValueError;

import lombok.Getter;

@Getter
public class RefreshToken {
	private static final Pattern PATTERN = Pattern.compile("^[A-Za-z0-9]{64}$");
	
	private final String value;
	
	private final TokenExpiration expiresAt;
	
	private RefreshToken(String value, TokenExpiration expiresAt) {
		this.value = value;
		this.expiresAt = expiresAt;
	}
	
	public static RefreshToken of(String value, TokenExpiration expiresAt) {
		refreshTokenValidate(value);
		return new RefreshToken(value, expiresAt);
	}
	
	public static RefreshToken ofFromCookie(String value) {
		refreshTokenValidate(value);
		return new RefreshToken(value, null);
	}
	
	private static void refreshTokenValidate(String value) {
		if(value == null || value.isBlank()) {
			throw new AuthException(AuthValueError.INVALID_REFRESH_TOKEN);
		}
		
		if(!PATTERN.matcher(value).matches()) {
			throw new AuthException(AuthValueError.INVALID_REFRESH_TOKEN);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RefreshToken other = (RefreshToken) obj;
		return Objects.equals(value, other.value);
	}
	
	
}