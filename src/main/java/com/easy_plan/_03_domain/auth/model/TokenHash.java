package com.easy_plan._03_domain.auth.model;

import java.util.Objects;
import java.util.regex.Pattern;

import com.easy_plan._03_domain.ValidErrorCode;
import com.easy_plan._03_domain.ValidException;

import lombok.Getter;

@Getter
public class TokenHash {
	private static final Pattern HASH_PATTERN = Pattern.compile("^[A-Za-z0-9+/]+=*$");
	
	private final String value;
	
	public TokenHash(String value) {
		validate(value);
		this.value = value;
	}

	private void validate(String value) {
		if(value == null || value.isBlank()) {
			throw new ValidException(new ValidErrorCode("TokenHash가 존재하지 않습니다.", "Security: [TokenHash Is Null]"));
		}
		
		if(value.length() != 44 && !HASH_PATTERN.matcher(value).matches()) {
			throw new ValidException(new ValidErrorCode("지원하지 않는 형식으로 해시된 TokenHash입니다.", "Security: [TokenHash Unsupported HashValue]"));
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
		TokenHash other = (TokenHash) obj;
		return Objects.equals(value, other.value);
	}
}
