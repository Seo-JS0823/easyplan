package com.easyplan._03_domain.user.model;

import java.util.regex.Pattern;

import com.easyplan._03_domain.user.exception.UserException;
import com.easyplan._03_domain.user.exception.UserValueError;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Email {
	private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
	
	private final String value;
	
	Email(String value) {
		this.value = value;
	}
	
	public static Email of(String value) {
		if(value == null || value.isBlank() || !EMAIL.matcher(value).matches()) {
			throw new UserException(UserValueError.INVALID_EMAIL);
		}
		
		return new Email(value);
	}
}
