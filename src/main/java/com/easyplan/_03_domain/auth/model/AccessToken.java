package com.easyplan._03_domain.auth.model;

import java.util.Objects;
import java.util.regex.Pattern;

import com.easyplan._03_domain.auth.exception.AuthException;
import com.easyplan._03_domain.auth.exception.AuthValueError;

import lombok.Getter;

@Getter
public class AccessToken {
	private static final Pattern JWT = Pattern.compile("^[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+$");
	
	private final String value;
	
	private final TokenClaims claims;
	
	private AccessToken(String value, TokenClaims claims) {
		this.value = value;
		this.claims = claims;
	}
	
	public static AccessToken of(String value, TokenClaims claims) {
		if(value == null || value.isBlank()) {
			throw new AuthException(AuthValueError.INVALID_ACCESS_TOKEN);
		}
		
		if(!JWT.matcher(value).matches()) {
			throw new AuthException(AuthValueError.INVALID_ACCESS_TOKEN);
		}
		
		return new AccessToken(value, claims);
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
		AccessToken other = (AccessToken) obj;
		return Objects.equals(value, other.value);
	}
}
