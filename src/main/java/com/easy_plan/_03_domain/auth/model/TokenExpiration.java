package com.easy_plan._03_domain.auth.model;

import java.time.Instant;
import java.util.Objects;

import com.easy_plan._03_domain.ValidErrorCode;
import com.easy_plan._03_domain.ValidException;

import lombok.Getter;

@Getter
public class TokenExpiration {
	private final Instant value;
	
	public TokenExpiration(Instant value) {
		validate(value);
		this.value = value;
	}
	
	private void validate(Instant value) {
		if(value == null) {
			throw new ValidException(new ValidErrorCode("만료 시간은 존재해야 합니다.", "Security: [TokenExpiration Is Null]"));
		}
	}
	
	public boolean isExpired(Instant now) {
		return value.isBefore(now);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		TokenExpiration other = (TokenExpiration) obj;
		return Objects.equals(value, other.value);
	}
}
