package com.easy_plan._03_domain.auth.model;

import java.util.Objects;
import java.util.regex.Pattern;

import com.easy_plan._03_domain.ValidErrorCode;
import com.easy_plan._03_domain.ValidException;

import lombok.Getter;

@Getter
public class RefreshToken {
	private static final Pattern REFRESH_TOKEN_PATTERN = Pattern.compile("^[A-Za-z0-9]{64}$");
	
	private final String value;
	
	private final Jti jti;
	
	private final TokenHash tokenHash;
	
	private final TokenExpiration expiration;
	
	public RefreshToken(String value, Jti jti, TokenHash tokenHash, TokenExpiration expiration) {
		validate(value);
		this.value = value;
		this.jti = jti;
		this.tokenHash = tokenHash;
		this.expiration = expiration;
	}
	
	private void validate(String value) {
		if(value == null || value.isBlank()) {
			throw new ValidException(new ValidErrorCode("리프레시 토큰의 원문이 존재하지 않습니다.", "Security: [RefreshToken Value Is Null]"));
		}
		
		if(!REFRESH_TOKEN_PATTERN.matcher(value).matches()) {
			throw new ValidException(new ValidErrorCode("지원하지 않는 형식의 리프레시 토큰입니다.", "Security: [Unsupported RefreshToken]"));
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
		RefreshToken other = (RefreshToken) obj;
		return Objects.equals(value, other.value);
	}
	
}
