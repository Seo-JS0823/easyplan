package com.easyplan.domain.user.model;

import java.util.regex.Pattern;

import com.easyplan.domain.user.error.UserErrorCode;
import com.easyplan.domain.user.error.UserException;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Email {
	private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
	
	private final String value;
	
	private Email(String value) {
		this.value = value;
	}
	
	public static Email of(String value) {
		if(value == null) {
			throw new UserException(UserErrorCode.INVALID_EMAIL);
		}
		
		String normalized = value.trim().toLowerCase();
		
		if(normalized.isBlank() || !EMAIL.matcher(normalized).matches()) {
			throw new UserException(UserErrorCode.INVALID_EMAIL);
		}
		
		return new Email(normalized);
	}
}
