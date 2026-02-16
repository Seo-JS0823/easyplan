package com.easyplan.domain.user.model;

import java.util.regex.Pattern;

import com.easyplan.domain.user.error.UserErrorCode;
import com.easyplan.domain.user.error.UserException;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class PasswordHash {
	private static final Pattern PASSWORD = Pattern.compile("^\\$2[ayb]\\$.{56}$");
	
	private final String value;
	
	private PasswordHash(String value) {
		this.value = value;
	}
	
	public String getHashedValue() {
		return value;
	}
	
	public static PasswordHash of(String value) {
		if(value == null || value.isBlank()) {
			throw new UserException(UserErrorCode.INVALID_PASSWORD_HASH);
		}
		
		if(!PASSWORD.matcher(value).matches()) {
			throw new UserException(UserErrorCode.INVALID_PASSWORD_HASH);
		}
		
		return new PasswordHash(value);
	}
}
