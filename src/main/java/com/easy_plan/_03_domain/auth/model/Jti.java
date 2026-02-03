package com.easy_plan._03_domain.auth.model;

import java.util.Objects;
import java.util.UUID;

import com.easy_plan._03_domain.ValidErrorCode;
import com.easy_plan._03_domain.ValidException;

import lombok.Getter;

@Getter
public class Jti {
	private final String value;
	
	public Jti(String value) {
		validate(value);
		this.value = value;
	}
	
	private void validate(String value) {
		if(value == null || value.isBlank()) {
			throw new ValidException(new ValidErrorCode("JTI는 Null일 수 없습니다.", "Security: [JTI Is Null]"));
		}
		
		try {
			UUID.fromString(value);
		} catch (IllegalArgumentException e) {
			throw new ValidException(new ValidErrorCode("지원하지 않는 형식의 JTI 입니다.", "Security: [JTI Unsupported]"));
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
		Jti other = (Jti) obj;
		return Objects.equals(value, other.value);
	}
}
