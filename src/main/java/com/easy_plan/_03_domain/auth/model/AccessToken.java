package com.easy_plan._03_domain.auth.model;

import java.util.Objects;
import java.util.regex.Pattern;

import com.easy_plan._03_domain.ValidErrorCode;
import com.easy_plan._03_domain.ValidException;

import lombok.Getter;

@Getter
public class AccessToken {
	private static final Pattern JWT_PATTERN = Pattern.compile("^[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+$");
	
	private static final int JWT_MIN_LENGTH = 50;
	
	private final String value;
	
	private final Jti jti;
	
	private final TokenExpiration expiration;
	
	public AccessToken(String value, Jti jti, TokenExpiration expiration) {
		validate(value);
		this.value = value;
		this.jti = jti;
		this.expiration = expiration;
	}
	
	private void validate(String value) {
		if(value == null || value.isBlank()) {
			throw new ValidException(new ValidErrorCode("액세스 토큰의 원문이 존재하지 않습니다.", "Security: [AccessToken is Null]"));
		}
		
		if(value.length() < JWT_MIN_LENGTH && !JWT_PATTERN.matcher(value).matches()) {
			throw new ValidException(new ValidErrorCode("액세스 토큰의 형식이 올바르지 않습니다.", "Security: [AccessToken Unsupported]"));
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		AccessToken other = (AccessToken) obj;
		return Objects.equals(value, other.value);
	}
}
