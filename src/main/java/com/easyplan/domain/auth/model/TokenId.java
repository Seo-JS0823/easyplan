package com.easyplan.domain.auth.model;

import java.util.UUID;
import java.util.regex.Pattern;

import com.easyplan.domain.auth.error.AuthSessionErrorCode;
import com.easyplan.domain.auth.error.AuthSessionException;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public final class TokenId {
	private static final Pattern UUID_PATTERN =
			Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$");
	
	private final String value;
	
	private TokenId(String value) {
		this.value = value;
	}
	
	public static TokenId of(String value) {
		if(value == null || value.isBlank()) {
			throw new AuthSessionException(AuthSessionErrorCode.INVALID_TOKEN_ID);
		}
		
		if(!UUID_PATTERN.matcher(value).matches()) {
			throw new AuthSessionException(AuthSessionErrorCode.INVALID_TOKEN_ID);
		}
		
		return new TokenId(value);
	}
	
	public static TokenId create() {
		return new TokenId(UUID.randomUUID().toString());
	}
}
